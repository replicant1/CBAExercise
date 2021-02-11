package com.bailey.rod.cbaexercise

import android.app.Application
import timber.log.Timber

class CbaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.i("Timber logging has been initialised")
        }
    }
}