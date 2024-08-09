package com.example.pair.domain.usecase

import com.example.pair.data.model.ExchangeResponse
import com.example.pair.domain.repository.ExchangeRatesRepository
import javax.inject.Inject

class GetExchangeRatesUseCase @Inject constructor(
    private val repository: ExchangeRatesRepository
): BaseUseCase<ExchangeResponse, EmptyRequest>() {
    override suspend fun invoke(params: EmptyRequest): ExchangeResponse = repository.get()
}