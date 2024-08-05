package com.example.pair

import android.app.Application
import com.example.pair.di.ApplicationComponent
import com.example.pair.di.DaggerApplicationComponent

class MyApplication: Application() {
    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.create()
    }
}