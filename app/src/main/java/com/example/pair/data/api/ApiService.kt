package com.example.pair.data.api

import com.example.pair.BuildConfig
import com.example.pair.data.model.ExchangeResponse
import retrofit2.http.GET

interface ApiService {
    @GET("/v6/${BuildConfig.API_KEY}/latest/USD")
    suspend fun get(): ExchangeResponse
}