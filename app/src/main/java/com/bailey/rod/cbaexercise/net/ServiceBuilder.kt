package com.bailey.rod.cbaexercise.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

//    fun provideClient(cache: Cache): OkHttpClient {
//        return OkHttpClient.Builder()
//            .cache(cache)
//            .addNetworkInterceptor(networkCacheInterceptor())
//            .addInterceptor(provideHttpLoggingInterceptor())
//            .connectTimeout(1, TimeUnit.MINUTES)
//            .readTimeout(1, TimeUnit.MINUTES)
//            .writeTimeout(2, TimeUnit.MINUTES)
//            .build()
    private val client = OkHttpClient.Builder().build()

    // .addCallAdapterFactory(LiveDataCallAdapterFactory())
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.dropbox.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}