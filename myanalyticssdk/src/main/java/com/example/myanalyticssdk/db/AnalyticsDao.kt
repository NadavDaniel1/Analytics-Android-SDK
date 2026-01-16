package com.example.myanalyticssdk.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
internal interface AnalyticsDao {

    @Insert
    suspend fun insertEvent(event: AnalyticsEvent)

    @Query("SELECT * FROM analytics_events")
    suspend fun getAllEvents(): List<AnalyticsEvent>

    @Query("DELETE FROM analytics_events WHERE id IN (:ids)")
    suspend fun deleteEvents(ids: List<Int>)

    @Query("SELECT COUNT(*) FROM analytics_events")
    suspend fun getEventCount(): Int
}