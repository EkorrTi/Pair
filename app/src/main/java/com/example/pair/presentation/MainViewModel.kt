package com.example.pair.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pair.data.model.ConversionRate
import com.example.pair.domain.usecase.EmptyRequest
import com.example.pair.presentation.state.ExchangeRateState
import com.example.pair.domain.usecase.GetExchangeRatesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface MainViewModel {

    interface Input {
        fun onGetExchangeRateClick()
        fun setCurrency(list: List<ConversionRate>, code: String)
        fun getConvertedValue(value: String)
    }

    interface Output {
        val listState: StateFlow<ExchangeRateState>
        val convertedValueState: StateFlow<Double>
    }

    class ViewModel @Inject constructor(
        private val useCase: GetExchangeRatesUseCase
    ) : androidx.lifecycle.ViewModel(), Input, Output {
        val input: Input = this
        val output: Output = this

        private val _listState = MutableStateFlow<ExchangeRateState>(ExchangeRateState.Idle)
        override val listState = _listState.asStateFlow()

        private val _convertedValueState = MutableStateFlow(0.0)
        override val convertedValueState = _convertedValueState.asStateFlow()

        private var rate: Double = 1.0

        override fun onGetExchangeRateClick() {
            viewModelScope.launch(Dispatchers.IO) {
                _listState.value = ExchangeRateState.Loading
                tryGetExchangeRates()
            }
        }

        override fun setCurrency(list: List<ConversionRate>, code: String) {
            val rate = list.find { it.code == code }
                ?.rate ?: 1.0

            this.rate = rate
        }

        override fun getConvertedValue(value: String) {
            viewModelScope.launch {
                val base = try {
                    value.toDouble()
                } catch (e: Exception) {
                    1.0
                }
                _convertedValueState.emit(rate * base)
            }
        }

        private suspend fun tryGetExchangeRates() {
            try {
                val data = useCase.invoke(EmptyRequest)
                _listState.emit(ExchangeRateState.Success(data))
            } catch (e: Exception) {
                _listState.emit(ExchangeRateState.Error("Error"))
            }
        }
    }
}

class ViewModelFactory<VM: ViewModel>(val provider: () -> VM): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  provider() as T
    }
}