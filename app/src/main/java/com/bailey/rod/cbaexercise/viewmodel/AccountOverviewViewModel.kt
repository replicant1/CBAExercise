package com.bailey.rod.cbaexercise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bailey.rod.cbaexercise.CbaApplication
import com.bailey.rod.cbaexercise.db.DbAccountOverview
import com.bailey.rod.cbaexercise.net.google.AbsentLiveData
import com.bailey.rod.cbaexercise.net.google.Resource
import com.bailey.rod.cbaexercise.repo.AccountRepository
import javax.inject.Inject

class AccountOverviewViewModel @Inject internal constructor(repo: AccountRepository) : ViewModel() {

    private val accountOverviewQuery = MutableLiveData<AccountOverviewQuery>()

    /**
     * Account overview as per latest AccountOverviewQuery. Clients should observe this.
     */
    val accountOverview: LiveData<Resource<DbAccountOverview>>

    /**
     * Zero based index of top-most item in list
     */
    val firstVisibleItemPosition = MutableLiveData<Int>()

    fun setAccountOverviewQuery(query: AccountOverviewQuery) {
        accountOverviewQuery.value = query
    }

    class AccountOverviewQuery(val forceFetch: Boolean)

    init {
        CbaApplication.graph.inject(this)

        accountOverview =
            Transformations.switchMap(accountOverviewQuery) { query: AccountOverviewQuery? ->
                if (query != null) {
                    return@switchMap repo.getAccountOverview(query.forceFetch)
                } else {
                    return@switchMap AbsentLiveData.create<Resource<DbAccountOverview>>()
                }
            }
    }

}

