package com.example.pair.di

import android.content.Context
import com.example.pair.MainActivity
import com.example.pair.presentation.MainViewModel
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class])
interface ApplicationComponent {
    val applicationContext: Context
    val mainViewModel: MainViewModel.ViewModel

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}