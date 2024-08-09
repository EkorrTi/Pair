package com.example.pair.presentation.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.pair.R
import com.example.pair.constants.Constants
import com.example.pair.presentation.MainViewModel
import com.example.pair.presentation.state.ExchangeRateState
import com.example.pair.presentation.theme.PairTheme
import java.text.DecimalFormat

@Composable
fun MainScreenUI(vm: MainViewModel.ViewModel, showToast: (String) -> Unit) {
    val listState by vm.output.listState.collectAsState()
    val convertedValueState by vm.output.convertedValueState.collectAsState()
    var baseCurrency by remember { mutableStateOf("") }

    if (listState is ExchangeRateState.Success) {
        vm.input.setCurrency(
            (listState as ExchangeRateState.Success)
                .data.conversionRates,
            "EUR"
        )
    }

    PairTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            MainScreen(
                listState,
                baseCurrency,
                onBaseCurrencyChange = {
                    baseCurrency = it
                    vm.input.getConvertedValue(it)
                },
                convertedValueState,
                getLatestOnClick = {
                    vm.input.onGetExchangeRateClick()
                },
                showToast = showToast,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun MainScreen(
    listState: ExchangeRateState,
    baseCurrency: String,
    onBaseCurrencyChange: (String) -> Unit,
    convertedValueState: Double,
    getLatestOnClick: () -> Unit,
    showToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val decimalFormat = DecimalFormat("#.####")
    Column(modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_16))) {
        Text(
            "Currency Exchange Rates",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Divider(
            modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_16)),
            thickness = dimensionResource(R.dimen.separator_thickness),
            color = Color.Gray
        )
        TextField(
            value = baseCurrency,
            label = { Text("Enter base currency") },
            onValueChange = onBaseCurrencyChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Divider(
            modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_16)),
            thickness = dimensionResource(R.dimen.separator_thickness),
            color = Color.Gray
        )
        Button(
            onClick = getLatestOnClick,
            enabled = true,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Get Latest Rates")
        }
        Divider(
            modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_16)),
            thickness = dimensionResource(R.dimen.separator_thickness),
            color = Color.Gray
        )
        Text("Base: USD")
        Text("Converted in EUR: ${decimalFormat.format(convertedValueState)}")
        Divider(
            modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_16)),
            thickness = dimensionResource(R.dimen.separator_thickness),
            color = Color.Gray
        )

        when (listState) {
            is ExchangeRateState.Loading -> {
                Text(
                    "Loading...",
                    fontSize = Constants.LOADING_FONT_SIZE.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            is ExchangeRateState.Success -> {
                LazyColumn {
                    items(listState.data.conversionRates.take(Constants.MAX_LIST_SIZE)) {
                        Text("${it.code} : ${it.rate}")
                    }
                }
            }

            is ExchangeRateState.Error -> showToast(listState.message)
            else -> Unit
        }
    }
}