package com.example.pair.di

import com.example.pair.MainActivity
import dagger.Component

@Component(modules = [AppModule::class])
interface ApplicationComponent {
    fun inject(activity: MainActivity)
}