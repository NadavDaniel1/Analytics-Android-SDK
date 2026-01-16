package com.example.myanalyticssdk.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Defines the HTTP API endpoints for communicating with the Analytics Backend Server.
 * Marked as 'internal' to keep network logic encapsulated within the SDK.
 */
internal interface AnalyticsApi {

    /**
     * Sends a batch of analytics events to the server.
     *
     * @param events A list of maps, where each map represents a JSON object of an event.
     * @return A Retrofit Call object to execute the request.
     */
    @POST("/analytics")
    @JvmSuppressWildcards // Crucial: Prevents Kotlin from generating wildcard types (e.g., ? extends Object), ensuring Retrofit/Gson serializes 'Any' correctly.
    fun sendEvents(@Body events: List<Map<String, Any>>): Call<Void>
}