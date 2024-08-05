package com.example.pair.state

import com.example.pair.data.ExchangeResponse

sealed class ExchangeRateState {
    data object Idle: ExchangeRateState()
    data object Loading : ExchangeRateState()
    data class Success(val data: ExchangeResponse) : ExchangeRateState()
    data class Error(val message: String) : ExchangeRateState()
}