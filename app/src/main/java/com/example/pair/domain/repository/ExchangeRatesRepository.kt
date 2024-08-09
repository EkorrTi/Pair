package com.example.pair.domain.repository

import com.example.pair.data.model.ExchangeResponse

interface ExchangeRatesRepository {
    suspend fun get(): ExchangeResponse
}