package com.bailey.rod.cbaexercise.dagger

import android.app.Application
import android.content.Context
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
    fun provideApp():Context = app

    @Provides
    @Singleton
    fun provideAccountRepository(): AccountRepository = AccountRepository()

}