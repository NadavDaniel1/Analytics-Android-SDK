package com.example.myanalyticssdk.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analytics_events")
internal data class AnalyticsEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventName: String,
    val jsonPayload: String, // נשמור את הפרמטרים כ-String פשוט כרגע
    val timestamp: Long = System.currentTimeMillis()
)