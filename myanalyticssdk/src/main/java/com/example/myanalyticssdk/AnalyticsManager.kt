package com.example.myanalyticssdk

import android.content.Context
import android.util.Log
import com.example.myanalyticssdk.db.AnalyticsDatabase
import com.example.myanalyticssdk.db.AnalyticsEvent
import com.example.myanalyticssdk.network.NetworkClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.awaitResponse

object AnalyticsManager {
    private const val TAG = "AnalyticsManager"
    private var database: AnalyticsDatabase? = null

    // Mutex to prevent race conditions during network synchronization.
    // This ensures that only one sync operation runs at a time.
    private val syncMutex = Mutex()

    /**
     * Initializes the SDK. Must be called once in the Application class or MainActivity.
     */
    fun init(context: Context) {
        if (database == null) {
            database = AnalyticsDatabase.getDatabase(context)
            Log.d(TAG, "SDK Initialized")
        }
    }

    /**
     * Tracks a new event.
     * The event is first saved locally to ensure no data loss, and then an attempt is made to sync with the server.
     */
    fun trackEvent(eventName: String, props: Map<String, Any>? = null) {
        if (database == null) return

        val jsonPayload = props?.toString() ?: "{}"

        CoroutineScope(Dispatchers.IO).launch {
            // 1. Local Storage: Save the event to the Room database first.
            // This guarantees that data is preserved even if the app crashes or has no internet.
            val event = AnalyticsEvent(eventName = eventName, jsonPayload = jsonPayload)
            database?.analyticsDao()?.insertEvent(event)
            Log.d(TAG, "Event saved to DB: $eventName")

            // 2. Synchronization: Attempt to sync pending events with the server.
            trySyncEvents()
        }
    }

    /**
     * Attempts to send pending events to the server.
     * Uses a Mutex to ensure atomic operations (Fetch -> Send -> Delete).
     */
    private suspend fun trySyncEvents() {
        // Optimization: If a sync is already running, skip this trigger.
        // The running sync will pick up the new events in the next batch or subsequent call.
        if (syncMutex.isLocked) {
            Log.d(TAG, "Sync already in progress, skipping duplicate trigger.")
            return
        }

        // Lock the critical section to prevent race conditions (duplicate data sending).
        syncMutex.withLock {
            val dao = database?.analyticsDao() ?: return@withLock

            // Retrieve all pending events from the local database
            val events = dao.getAllEvents()
            if (events.isEmpty()) return@withLock

            Log.d(TAG, "Attempting to send ${events.size} events to server (Thread Safe)...")

            // Convert events to the format expected by the server API
            val eventsForServer = events.map {
                mapOf(
                    "id" to it.id,
                    "event" to it.eventName,
                    "data" to it.jsonPayload,
                    "time" to it.timestamp
                )
            }

            try {
                // Perform the network request using Retrofit
                val response = NetworkClient.api.sendEvents(eventsForServer).awaitResponse()

                if (response.isSuccessful) {
                    Log.d(TAG, "Success! Server received events.")

                    // Critical Step: Only delete events after a successful server response.
                    // This prevents data loss if the network fails.
                    dao.deleteEvents(events.map { it.id })
                    Log.d(TAG, "Events deleted from local DB.")
                } else {
                    Log.e(TAG, "Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                // Network failure is expected occasionally. Events remain in DB for next retry.
                Log.e(TAG, "Network failed: ${e.message}")
            }
        }
    }
}