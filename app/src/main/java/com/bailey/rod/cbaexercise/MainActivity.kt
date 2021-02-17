package com.bailey.rod.cbaexercise

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bailey.rod.cbaexercise.data.XAccountActivitySummary
import com.bailey.rod.cbaexercise.databinding.ActivityMainBinding
import com.bailey.rod.cbaexercise.db.DbAccountActivitySummary
import com.bailey.rod.cbaexercise.net.google.Resource
import com.bailey.rod.cbaexercise.net.google.Status
import com.bailey.rod.cbaexercise.ui.TxListAdapter
import com.bailey.rod.cbaexercise.viewmodel.MainActivityViewModel
import com.google.gson.Gson
import timber.log.Timber

/**
 * Summary of account activity as supplied by server.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding.txSwipeRefresh.setOnRefreshListener { forceFetchData() }

        observeViewModel()

        // If the view model already has data then use it - else wait for new data to
        // be observed. Former occurs on orientation changes.
//        if (viewModel.accountActivitySummary.value == null) {
//            fetchData()
//        }
    }

    /*
     * Note we don't observe viewModel.firstVisibleListPosition. We get its value synchronously
     * after setting new list data (see #handleFetchedData)
     */
    private fun observeViewModel() {
        viewModel.getAccountActivitySummary(forceFetch = false).observe(this, Observer {
            val resource: Resource<DbAccountActivitySummary> = it
            if (resource.status == Status.SUCCESS) {
                Timber.d("Trying to parse this JSON: ${resource.data?.summaryJson}")
                val xAccountActivitySummary = Gson().fromJson(resource.data?.summaryJson, XAccountActivitySummary::class.java)
                handleFetchedData(xAccountActivitySummary)
            } else {
                Timber.w("Status of account activity summary retrieval = ${resource.status}")
            }
        })
    }

    private fun forceFetchData() {
        binding.txSwipeRefresh.isRefreshing = true
//        if (BuildConfig.UseLocalData) {
//            viewModel.loadSyncAccountActivitySummary(this)
//        } else {
//            viewModel.fetchAsyncAccountActivitySummary()
//        }
    }

    private fun handleFetchedData(accountSummary: XAccountActivitySummary?) {
        if (accountSummary != null) {
            val linearLayoutManager = LinearLayoutManager(this)

            binding.rvTxList.layoutManager = linearLayoutManager
            binding.rvTxList.adapter = TxListAdapter(this, accountSummary)

            val posToRestore = viewModel.firstVisibleItemPosition.value
            val currentPos = linearLayoutManager.findFirstVisibleItemPosition()

            if ((currentPos != posToRestore) && (posToRestore != null)) {
                binding.rvTxList.scrollToPosition(posToRestore)
            }

            binding.rvTxList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val position = linearLayoutManager.findFirstVisibleItemPosition()
                        viewModel.firstVisibleItemPosition.value = position
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    // Empty
                }
            })

        } else {
            Toast.makeText(this, getString(R.string.fail_account_activity_load), Toast.LENGTH_LONG)
                .show()
        }
        binding.txSwipeRefresh.isRefreshing = false
    }

}