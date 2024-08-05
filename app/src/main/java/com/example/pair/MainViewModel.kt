package com.example.pair

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pair.state.ExchangeRateState
import com.example.pair.usecase.MyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val useCase: MyUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<ExchangeRateState>(ExchangeRateState.Idle)
    val state = _state.asStateFlow()

    fun getExchangeRate() {
        viewModelScope.launch {
            _state.value = ExchangeRateState.Loading
            try {
                val data = useCase.invoke()
                _state.value = ExchangeRateState.Success(data)
                Log.d("RETRO", data.toString())
            } catch (e: Exception) {
                _state.value = ExchangeRateState.Error("Error")
                Log.d("RETRO", e.toString())
            }
        }
    }
}
