package com.example.myanalyticssdk.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The Room Database class for the Analytics SDK.
 * Marked as 'internal' to restrict access to within the SDK module,
 * preventing external apps from accessing the database directly.
 */
@Database(entities = [AnalyticsEvent::class], version = 1, exportSchema = false)
internal abstract class AnalyticsDatabase : RoomDatabase() {

    // Abstract method to access the Data Access Object (DAO)
    abstract fun analyticsDao(): AnalyticsDao

    companion object {
        // Volatile ensures that the instance is immediately visible to other threads
        @Volatile
        private var INSTANCE: AnalyticsDatabase? = null

        /**
         * Returns the singleton instance of the database.
         * Implements the Singleton pattern to ensure only one database connection exists at a time.
         */
        fun getDatabase(context: Context): AnalyticsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Use Application Context to prevent memory leaks
                    AnalyticsDatabase::class.java,
                    "analytics_sdk_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}