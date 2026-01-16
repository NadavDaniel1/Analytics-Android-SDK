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

    fun init(context: Context) {
        if (database == null) {
            database = AnalyticsDatabase.getDatabase(context)
            Log.d(TAG, "SDK Initialized")
        }
    }

    fun trackEvent(eventName: String, props: Map<String, Any>? = null) {
        if (database == null) return

        val jsonPayload = props?.toString() ?: "{}"

        CoroutineScope(Dispatchers.IO).launch {
            // 1. שמירה לוקאלית (כמו קודם)
            val event = AnalyticsEvent(eventName = eventName, jsonPayload = jsonPayload)
            database?.analyticsDao()?.insertEvent(event)
            Log.d(TAG, "Event saved to DB: $eventName")

            // 2. ניסיון שליחה לשרת (החלק החדש!)
            trySyncEvents()
        }
    }

    private suspend fun trySyncEvents() {
        val dao = database?.analyticsDao() ?: return

        // נשלוף את כל האירועים שמחכים ב-DB
        val events = dao.getAllEvents()
        if (events.isEmpty()) return

        Log.d(TAG, "Attempting to send ${events.size} events to server...")

        // המרה לפורמט שהשרת מצפה לו
        val eventsForServer = events.map {
            mapOf(
                "id" to it.id,
                "event" to it.eventName,
                "data" to it.jsonPayload,
                "time" to it.timestamp
            )
        }

        try {
            // שליחה בפועל דרך רטרופיט
            val response = NetworkClient.api.sendEvents(eventsForServer).awaitResponse()

            if (response.isSuccessful) {
                Log.d(TAG, "Success! Server received events.")
                // אם השרת קיבל - מוחקים מהטלפון כדי שלא נשלח שוב ושוב
                dao.deleteEvents(events.map { it.id })
                Log.d(TAG, "Events deleted from local DB.")
            } else {
                Log.e(TAG, "Server error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network failed: ${e.message}")
        }
    }
}