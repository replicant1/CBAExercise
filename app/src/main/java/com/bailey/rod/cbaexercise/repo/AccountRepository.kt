package com.bailey.rod.cbaexercise.repo

import timber.log.Timber
import javax.inject.Singleton

@Singleton
class AccountRepository(){
    init {
        Timber.d("Initializing AccountRepository ${hashCode()}")
    }
}