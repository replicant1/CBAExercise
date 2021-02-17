package com.bailey.rod.cbaexercise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bailey.rod.cbaexercise.CbaApplication
import com.bailey.rod.cbaexercise.db.DbAccountActivitySummary
import com.bailey.rod.cbaexercise.net.google.Resource
import com.bailey.rod.cbaexercise.repo.AccountRepository
import javax.inject.Inject

class MainActivityViewModel() : ViewModel() {

    init {
        CbaApplication.graph.inject(this)

//        accountActivitySummary = Transformations.switchMap()
    }

    @Inject
    lateinit var accountRepository: AccountRepository

//    var accountActivitySummary : LiveData<ApiResponse<XAccountActivitySummary>>? = null

//    private val _accountActivitySummary = MutableLiveData<XAccountActivitySummary>()

    // Expose the read-only version of the accountActivitySummary
//    val accountActivitySummary: LiveData<XAccountActivitySummary> = _accountActivitySummary

    // Zero based index of top-most item in list
    val firstVisibleItemPosition = MutableLiveData<Int>()


    fun getAccountActivitySummary(forceFetch: Boolean): LiveData<Resource<DbAccountActivitySummary>> {
        return accountRepository.getAccountActivitySummary(forceFetch)
    }

}