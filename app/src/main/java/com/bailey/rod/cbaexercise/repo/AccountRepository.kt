package com.bailey.rod.cbaexercise.repo

import androidx.lifecycle.LiveData
import com.bailey.rod.cbaexercise.BuildConfig
import com.bailey.rod.cbaexercise.CbaApplication
import com.bailey.rod.cbaexercise.data.XAccountOverview
import com.bailey.rod.cbaexercise.db.DbAccountOverview
import com.bailey.rod.cbaexercise.db.DbAccountOverviewDao
import com.bailey.rod.cbaexercise.net.google.ApiResponse
import com.bailey.rod.cbaexercise.net.google.AppExecutors
import com.bailey.rod.cbaexercise.net.google.NetworkBoundResource
import com.bailey.rod.cbaexercise.net.google.Resource
import com.bailey.rod.cbaexercise.net.service.CbaAccountService
import com.google.gson.Gson
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data source for account related information. Decides whether to fetch from a network source
 * or load from a local database source.
 */
@Singleton
class AccountRepository() {
    init {
        CbaApplication.graph.inject(this)
    }

    @Inject
    lateinit var cbaAccountService: CbaAccountService

    @Inject
    lateinit var dbAccountOverviewDao: DbAccountOverviewDao

    @Inject
    lateinit var appExecutors: AppExecutors

    /**
     *
     */
    fun getAccountOverview(forceFetch: Boolean): LiveData<Resource<DbAccountOverview>> {
        Timber.i("Fetching account overview from network")

        return object :
            NetworkBoundResource<DbAccountOverview, XAccountOverview>(appExecutors) {
            override fun saveCallResult(item: XAccountOverview) {
                Timber.d("saveCallResult: result to save = $item")
                dbAccountOverviewDao.deleteAll()

                val accountNumber = item.account?.accountNumber ?: "?????"
                val fetchTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
                val overviewJson = Gson().toJson(item)

                val newDbRecord = DbAccountOverview(accountNumber,fetchTime, overviewJson)

                Timber.d("saveCallResult: db record to save = $newDbRecord")

                dbAccountOverviewDao.insert(newDbRecord);
            }

            override fun shouldFetch(data: DbAccountOverview?): Boolean {
                val result = forceFetch || data == null
                Timber.d("shouldFetch: forceFetch = $forceFetch, result = $result, data = $data")
                return result
            }

            override fun loadFromDb(): LiveData<DbAccountOverview> {
                Timber.d(
                    "loadFromDb. There are %s records in the db",
                    dbAccountOverviewDao.getAllSync().size
                )
                return dbAccountOverviewDao.get()
            }

            override fun createCall(): LiveData<ApiResponse<XAccountOverview>> {
                Timber.d("createCall: Creating call to cbaService")
                return cbaAccountService.getAccountOverview(
                    BuildConfig.CbaServiceKey,
                    BuildConfig.CbaServiceFileName,
                    BuildConfig.CbaServiceDownloadFlag
                )
            }

        }.asLiveData()
    }

}