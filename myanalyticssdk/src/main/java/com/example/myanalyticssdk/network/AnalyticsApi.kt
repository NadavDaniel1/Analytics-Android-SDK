package com.example.myanalyticssdk.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AnalyticsApi {
    @POST("/analytics")
    @JvmSuppressWildcards // <--- הנה המיקום הנכון! (מעל הפונקציה)
    fun sendEvents(@Body events: List<Map<String, Any>>): Call<Void>
}