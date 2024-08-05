package com.example.pair.usecase

import com.example.pair.data.ExchangeResponse
import com.example.pair.repository.MyRepository
import javax.inject.Inject

class MyUseCase @Inject constructor(
    private val repository: MyRepository
) {
    suspend fun invoke(): ExchangeResponse = repository.get()
}