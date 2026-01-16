package com.example.myanalyticssdk.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object NetworkClient {

    // כתובת הקסם של האימולטור!
    // 10.0.2.2 באנדרואיד = המחשב המארח (Localhost)
    private const val BASE_URL = "http://10.0.2.2:5000"

    val api: AnalyticsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnalyticsApi::class.java)
    }
}