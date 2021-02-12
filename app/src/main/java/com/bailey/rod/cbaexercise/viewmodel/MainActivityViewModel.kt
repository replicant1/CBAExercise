package com.bailey.rod.cbaexercise.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bailey.rod.cbaexercise.BuildConfig
import com.bailey.rod.cbaexercise.assetFileAsString
import com.bailey.rod.cbaexercise.data.XAccountActivitySummary
import com.bailey.rod.cbaexercise.net.CbaService
import com.bailey.rod.cbaexercise.net.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MainActivityViewModel() : ViewModel() {

    val accountActivitySummary = MutableLiveData<XAccountActivitySummary>()

    fun fetchAsyncAccountActivitySummary() {
        Timber.i("Fetching account activity summary from network")

        val request = ServiceBuilder.buildService(CbaService::class.java)
        val call =
            request.getAccountActivitySummary(
                BuildConfig.CbaServiceKey,
                BuildConfig.CbaServiceFileName,
                BuildConfig.CbaServiceDownloadFlag
            )

        call.enqueue(object : Callback<XAccountActivitySummary> {
            override fun onResponse(
                call: Call<XAccountActivitySummary>,
                response: Response<XAccountActivitySummary>
            ) {
                if (response.isSuccessful) {
                    Timber.d("Response from server: $response")
                    val summary: XAccountActivitySummary? = response.body()
                    if (summary != null) {
                        accountActivitySummary.postValue(summary)
                    } else {
                        accountActivitySummary.postValue(null)
                    }
                } else {
                    accountActivitySummary.postValue(null)
                }
            }

            override fun onFailure(call: Call<XAccountActivitySummary>, t: Throwable) {
                accountActivitySummary.postValue(null)
            }
        })
    }

    fun loadSyncAccountActivitySummary(ctx: Context): XAccountActivitySummary {
        val jsonString = ctx.assetFileAsString(BuildConfig.SampleAccountDataFile)
        return XAccountActivitySummary.parse(jsonString)
    }
}