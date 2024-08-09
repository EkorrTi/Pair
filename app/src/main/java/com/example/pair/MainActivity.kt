package com.example.pair

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.pair.di.ApplicationComponent
import com.example.pair.presentation.MainViewModel
import com.example.pair.presentation.ViewModelFactory
import com.example.pair.presentation.compose.MainScreenUI

class MainActivity : ComponentActivity() {

    private lateinit var appComponent: ApplicationComponent
    private lateinit var vm: MainViewModel.ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent = (applicationContext as MyApplication).appComponent
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(
            this,
            ViewModelFactory { appComponent.mainViewModel }
        )[MainViewModel.ViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            MainScreenUI(vm = vm) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}


