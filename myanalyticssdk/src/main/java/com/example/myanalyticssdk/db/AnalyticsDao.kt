package com.example.myanalyticssdk.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Data Access Object (DAO) for managing analytics events in the local database.
 * Marked as 'internal' so it is only accessible within the SDK module.
 */
@Dao
internal interface AnalyticsDao {

    /**
     * Inserts a new event into the database.
     * This is a suspend function to ensure it runs on a background thread.
     */
    @Insert
    suspend fun insertEvent(event: AnalyticsEvent)

    /**
     * Retrieves all stored events.
     * Used to fetch pending events before attempting to sync them with the server.
     */
    @Query("SELECT * FROM analytics_events")
    suspend fun getAllEvents(): List<AnalyticsEvent>

    /**
     * Deletes a specific list of events by their IDs.
     * This should be called only after the events have been successfully sent to the server.
     */
    @Query("DELETE FROM analytics_events WHERE id IN (:ids)")
    suspend fun deleteEvents(ids: List<Int>)

    /**
     * Returns the total number of events currently stored in the database.
     * Useful for debugging or determining if a sync is needed.
     */
    @Query("SELECT COUNT(*) FROM analytics_events")
    suspend fun getEventCount(): Int
}