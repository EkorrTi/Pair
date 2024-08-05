package com.example.pair.repository

import com.example.pair.ApiService
import com.example.pair.data.ExchangeResponse
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val retrofit: ApiService
) : MyRepository {
    override suspend fun get(): ExchangeResponse = retrofit.get()
}