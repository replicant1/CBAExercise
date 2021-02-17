package com.bailey.rod.cbaexercise.repo

import androidx.lifecycle.LiveData
import com.bailey.rod.cbaexercise.BuildConfig
import com.bailey.rod.cbaexercise.CbaApplication
import com.bailey.rod.cbaexercise.data.XAccountActivitySummary
import com.bailey.rod.cbaexercise.db.DbAccountActivitySummary
import com.bailey.rod.cbaexercise.db.DbAccountActivitySummaryDao
import com.bailey.rod.cbaexercise.net.CbaService
import com.bailey.rod.cbaexercise.net.google.ApiResponse
import com.bailey.rod.cbaexercise.net.google.AppExecutors
import com.bailey.rod.cbaexercise.net.google.NetworkBoundResource
import com.bailey.rod.cbaexercise.net.google.Resource
import com.google.gson.Gson
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 */
@Singleton
class AccountRepository() {
    init {
        CbaApplication.graph.inject(this)
    }

    @Inject
    lateinit var cbaService: CbaService

    @Inject
    lateinit var dbAccountActivitySummaryDao: DbAccountActivitySummaryDao

    @Inject
    lateinit var appExecutors: AppExecutors


    fun hasAccountActivitySummaryInDb(): Boolean {
        return dbAccountActivitySummaryDao.getSync() != null
    }

    fun loadAccountActivitySummaryFromDb(): XAccountActivitySummary? {
        val summaryInDb = dbAccountActivitySummaryDao.getSync()
        if (summaryInDb != null) {
            return XAccountActivitySummary.parse(summaryInDb.summaryJson)
        }
        return null
    }

    fun getAccountActivitySummary(forceFetch: Boolean): LiveData<Resource<DbAccountActivitySummary>> {
        Timber.i("Fetching account activity summary from network")
        Timber.d("Injected CbaService = ${cbaService.hashCode()}")

        return object :
            NetworkBoundResource<DbAccountActivitySummary, XAccountActivitySummary>(appExecutors) {
            override fun saveCallResult(item: XAccountActivitySummary) {
                Timber.d("saveCallResult: result to save = $item")
                dbAccountActivitySummaryDao.deleteAll()

                val accountNumber = item.account?.accountNumber ?: "?????"
                val summaryJson = Gson().toJson(item)
                val newDbRecord = DbAccountActivitySummary(accountNumber, summaryJson)

                Timber.d("saveCallResult: db record to save = $newDbRecord")

                dbAccountActivitySummaryDao.insert(newDbRecord);
            }

            override fun shouldFetch(data: DbAccountActivitySummary?): Boolean {
                val result = forceFetch || data == null
                Timber.d("shouldFetch: forceFetch = $forceFetch, result = $result, data = $data")
                return result
            }

            override fun loadFromDb(): LiveData<DbAccountActivitySummary> {
                Timber.d(
                    "loadFromDb. There are %s records in the db",
                    dbAccountActivitySummaryDao.getAllSync().size
                )
                return dbAccountActivitySummaryDao.get()
            }

            override fun createCall(): LiveData<ApiResponse<XAccountActivitySummary>> {
                Timber.d("createCall: Creating call to cbaService")
                return cbaService.getAccountActivitySummary(
                    BuildConfig.CbaServiceKey,
                    BuildConfig.CbaServiceFileName,
                    BuildConfig.CbaServiceDownloadFlag
                )
            }

        }.asLiveData()
    }


//        call.enqueue(object : Callback<XAccountActivitySummary> {
//            override fun onResponse(
//                call: Call<XAccountActivitySummary>,
//                response: Response<XAccountActivitySummary>
//            ) {
//                if (response.isSuccessful) {
//                    Timber.d("Response from server: $response")
//                    val summary: XAccountActivitySummary? = response.body()
//                    if (summary != null) {
//                        _accountActivitySummary.value = summary
//                        //firstVisibleItemPosition.value = 0
//                    } else {
//                        _accountActivitySummary.value = null
//                    }
//                } else {
//                    _accountActivitySummary.value = null
//                }
//            }
//
//            override fun onFailure(call: Call<XAccountActivitySummary>, t: Throwable) {
//                _accountActivitySummary.value = null
//            }
//        })
//    }

//    fun loadSyncAccountActivitySummary(ctx: Context): XAccountActivitySummary {
//        val jsonString = ctx.assetFileAsString(BuildConfig.SampleAccountDataFile)
//        return XAccountActivitySummary.parse(jsonString)
//    }
}