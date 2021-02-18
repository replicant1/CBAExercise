package com.bailey.rod.cbaexercise.net.service

import com.bailey.rod.cbaexercise.BuildConfig
import com.bailey.rod.cbaexercise.net.google.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {

    private val client = OkHttpClient.Builder()
        .connectTimeout(BuildConfig.ConnectTimeoutMinutes, TimeUnit.MINUTES)
        .readTimeout(BuildConfig.ReadTimeoutMinutes, TimeUnit.MINUTES)
        .writeTimeout(BuildConfig.WriteTimeoutMinutes, TimeUnit.MINUTES)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.ServiceBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .client(client)
        .build()

    fun <T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}