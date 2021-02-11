package com.bailey.rod.cbaexercise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
                SERVICE_KEY,
                SERVICE_FILE_NAME,
                SERVICE_DOWNLOAD_FLAG
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

    //    private fun parseJson(): XAccountActivitySummary {
//        val jsonString = applicationContext.assetFileAsString(SAMPLE_ACCOUNT_DATA_FILE)
//        return XAccountActivitySummary.parse(jsonString)
//    }

    companion object {
        //        const val SAMPLE_ACCOUNT_DATA_FILE = "sample_account_data.json"
        const val SERVICE_KEY = "tewg9b71x0wrou9"
        const val SERVICE_FILE_NAME = "data.json"
        const val SERVICE_DOWNLOAD_FLAG = 1
    }
}