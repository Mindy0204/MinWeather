package com.mindyhsu.minweather

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MinWeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.TIMBER_VISIABLE) {
            Timber.plant(Timber.DebugTree())
        }
    }
}