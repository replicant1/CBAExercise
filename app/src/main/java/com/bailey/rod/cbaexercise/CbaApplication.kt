package com.bailey.rod.cbaexercise

import android.app.Application
import com.bailey.rod.cbaexercise.dagger.AppComponent
import com.bailey.rod.cbaexercise.dagger.AppModule
import com.bailey.rod.cbaexercise.dagger.DaggerAppComponent
import timber.log.Timber

class CbaApplication : Application() {

    lateinit var cbaComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        graph = createComponent(this)
        graph.inject(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.i("Timber logging has been initialised")
        }
    }

    private fun createComponent(app: Application): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()

    companion object {
        @JvmStatic
        lateinit var graph: AppComponent
    }
}