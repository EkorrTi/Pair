package com.example.pair.data.repository

import com.example.pair.data.api.ApiService
import com.example.pair.data.model.ExchangeResponse
import com.example.pair.domain.repository.ExchangeRatesRepository
import javax.inject.Inject

class ExchangeRatesRepositoryImpl @Inject constructor(
    private val retrofit: ApiService
) : ExchangeRatesRepository {
    override suspend fun get(): ExchangeResponse = retrofit.get()
}