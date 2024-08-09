package com.example.pair.presentation.state

import com.example.pair.data.model.ExchangeResponse

sealed class ExchangeRateState {
    data object Idle: ExchangeRateState()
    data object Loading : ExchangeRateState()
    data class Success(val data: ExchangeResponse) : ExchangeRateState()
    data class Error(val message: String) : ExchangeRateState()
}