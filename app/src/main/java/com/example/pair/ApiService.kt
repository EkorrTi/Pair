package com.example.pair

import com.example.pair.data.ExchangeResponse
import retrofit2.http.GET

interface ApiService {
    @GET("/v6/27c7933ef07ca0cc06a6f431/latest/USD")
    suspend fun get(): ExchangeResponse
}
