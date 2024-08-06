package com.example.pair

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pair.state.ExchangeRateState
import com.example.pair.ui.theme.PairTheme
import java.text.DecimalFormat
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val state by vm.state.collectAsState()
            var baseCurrency by remember { mutableStateOf("") }

            PairTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        state,
                        baseCurrency,
                        onBaseCurrencyChange = { baseCurrency = it },
                        getLatestOnClick = {
                            vm.getExchangeRate()
                        },
                        showToast = {
                            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    state: ExchangeRateState,
    baseCurrency: String,
    onBaseCurrencyChange: (String) -> Unit,
    getLatestOnClick: () -> Unit,
    showToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val decimalFormat = DecimalFormat("#.####")
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(
            "Currency Exchange Rates",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 2.dp, color = Color.Gray)
        TextField(
            value = baseCurrency,
            label = { Text("Enter base currency") },
            onValueChange = onBaseCurrencyChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 2.dp, color = Color.Gray)
        Button(
            onClick = getLatestOnClick,
            enabled = true,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Get Latest Rates")
        }
        Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 2.dp, color = Color.Gray)
        Text("Base: USD")
        Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 2.dp, color = Color.Gray)

        when (state) {
            is ExchangeRateState.Loading -> {
                Text(
                    // CircularProgressIndicator() crashes app, problem seems to be with versions, but it uses the initial compose dependency wth
                    "Loading...",
                    fontSize = 36.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            is ExchangeRateState.Success -> {
                Row {
                    LazyColumn {
                        items(state.data.conversionRates.subList(0, 10)) {
                            Text("${it.code} : ${it.rate}")
                        }
                    }
                    Box(modifier = Modifier.weight(1f))
                    LazyColumn {
                        items(state.data.conversionRates.subList(0, 10)) {
                            val base = try {
                                baseCurrency.toDouble()
                            } catch (e: Exception) {
                                1.0
                            }
                            val exchange = decimalFormat.format(it.rate * base)
                            Text("${it.code} : $exchange")
                        }
                    }
                }
            }

            is ExchangeRateState.Error -> showToast(state.message)
            else -> Unit
        }
    }
}
