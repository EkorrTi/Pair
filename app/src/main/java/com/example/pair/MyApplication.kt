package com.example.pair

import android.app.Application
import com.example.pair.di.ApplicationComponent
import com.example.pair.di.DaggerApplicationComponent

class MyApplication: Application() {
    val appComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }
}