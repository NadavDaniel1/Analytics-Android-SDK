package com.example.myanalyticssdk.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AnalyticsEvent::class], version = 1, exportSchema = false)
internal abstract class AnalyticsDatabase : RoomDatabase() {

    abstract fun analyticsDao(): AnalyticsDao

    companion object {
        @Volatile
        private var INSTANCE: AnalyticsDatabase? = null

        fun getDatabase(context: Context): AnalyticsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnalyticsDatabase::class.java,
                    "analytics_sdk_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}