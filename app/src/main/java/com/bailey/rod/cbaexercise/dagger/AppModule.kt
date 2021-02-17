package com.bailey.rod.cbaexercise.dagger

import android.app.Application
import androidx.room.Room
import com.bailey.rod.cbaexercise.db.AppDatabase
import com.bailey.rod.cbaexercise.net.CbaService
import com.bailey.rod.cbaexercise.net.ServiceBuilder
import com.bailey.rod.cbaexercise.repo.AccountRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Any class to be injected by Dagger must be provided by a method here.
 */
@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideApp(): Application = app

    @Provides
    @Singleton
    fun provideAccountRepository(): AccountRepository = AccountRepository()

    @Singleton
    @Provides
    fun provideCbaService(): CbaService {
        return ServiceBuilder.buildService(CbaService::class.java)
    }

}