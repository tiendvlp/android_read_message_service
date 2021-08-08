package com.devlogs.client_android.common

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application () {
    override fun onCreate() {
        super.onCreate()
    }
}