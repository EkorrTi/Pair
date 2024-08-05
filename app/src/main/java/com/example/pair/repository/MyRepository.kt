package com.example.pair.repository

import com.example.pair.data.ExchangeResponse

interface MyRepository {
    suspend fun get(): ExchangeResponse
}