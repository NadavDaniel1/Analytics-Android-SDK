package com.example.myanalyticssdk

import android.content.Context
import android.util.Log
import com.example.myanalyticssdk.db.AnalyticsDatabase
import com.example.myanalyticssdk.db.AnalyticsEvent
import com.example.myanalyticssdk.network.NetworkClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

object AnalyticsManager {
    private const val TAG = "AnalyticsManager"
    private var database: AnalyticsDatabase? = null

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
            val event = AnalyticsEvent(eventName = eventName, jsonPayload = jsonPayload)
            database?.analyticsDao()?.insertEvent(event)
            Log.d(TAG, "Event saved to DB: $eventName")

            // 2. Synchronization: Attempt to sync pending events with the server.
            trySyncEvents()
        }
    }

    private suspend fun trySyncEvents() {
        val dao = database?.analyticsDao() ?: return

        // Retrieve all pending events from the local database
        val events = dao.getAllEvents()
        if (events.isEmpty()) return

        Log.d(TAG, "Attempting to send ${events.size} events to server...")

        // Convert events to the format expected by the server
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
                // On success: Clear events from local storage to prevent duplicate data
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