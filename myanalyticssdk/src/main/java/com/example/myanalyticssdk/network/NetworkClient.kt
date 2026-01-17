package com.example.myanalyticssdk.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object responsible for creating and managing the Retrofit network client.
 * Marked as 'internal' to ensure network configuration remains private within the SDK.
 */
internal object NetworkClient {

    /**
     * The base URL for the Analytics Server.
     * NOTE: "10.0.2.2" is a special IP address used by the Android Emulator
     * to connect to "localhost" on the developer's host machine.
     */
    private const val BASE_URL = "https://analytics-server-nadav.onrender.com/"

    /**
     * The singleton instance of the AnalyticsApi.
     * Initialized using 'by lazy' to ensure the Retrofit client is built
     * only when it is accessed for the first time, saving resources.
     */
    val api: AnalyticsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Handles JSON serialization/deserialization
            .build()
            .create(AnalyticsApi::class.java)
    }
}