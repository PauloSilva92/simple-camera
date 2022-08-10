package com.example.simplecamera

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SimpleCameraApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}