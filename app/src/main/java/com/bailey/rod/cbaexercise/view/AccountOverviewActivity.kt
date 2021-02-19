package com.bailey.rod.cbaexercise.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bailey.rod.cbaexercise.R
import com.bailey.rod.cbaexercise.data.XAccountOverview
import com.bailey.rod.cbaexercise.databinding.ActivityAccountOverviewBinding
import com.bailey.rod.cbaexercise.db.DbAccountOverview
import com.bailey.rod.cbaexercise.net.google.Resource
import com.bailey.rod.cbaexercise.net.google.Status
import com.bailey.rod.cbaexercise.viewmodel.AccountOverviewViewModel
import com.google.gson.Gson
import timber.log.Timber

/**
 * Summary of account activity as supplied by server.
 */
class AccountOverviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountOverviewBinding
    private lateinit var viewModel: AccountOverviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountOverviewBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(AccountOverviewViewModel::class.java)

        setContentView(binding.root)

        binding.txSwipeRefresh.setOnRefreshListener {
            viewModel.setAccountOverviewQuery(AccountOverviewViewModel.AccountOverviewQuery(true))
        }

        observeViewModel()

        viewModel.setAccountOverviewQuery(AccountOverviewViewModel.AccountOverviewQuery(false))
    }

    /*
     * Note we don't observe viewModel.firstVisibleListPosition. We get its value synchronously
     * after setting new list data from viewModel.accountOverview
     */
    private fun observeViewModel() {
        viewModel.accountOverview.observe(this, Observer {
            val resource: Resource<DbAccountOverview> = it
            when (resource.status) {
                Status.SUCCESS -> {
                    Timber.d("Trying to parse this JSON: ${resource.data?.overviewJson}")
                    val overview =
                        Gson().fromJson(resource.data?.overviewJson, XAccountOverview::class.java)
                    handleFetchSuccess(overview)
                    binding.txSwipeRefresh.isRefreshing = false
                }
                Status.LOADING -> {
                    binding.txSwipeRefresh.isRefreshing = true
                }
                Status.ERROR -> {
                    handleFetchError()
                    binding.txSwipeRefresh.isRefreshing = false
                }
            }
        })
    }

    private fun handleFetchSuccess(overview: XAccountOverview?) {
        Timber.i("Into handleFetchedData with overview = $overview")

        if (overview != null) {
            val linearLayoutManager = LinearLayoutManager(this)

            binding.rvTxList.layoutManager = linearLayoutManager
            binding.rvTxList.adapter = TxListAdapter(this, overview)

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
            handleFetchError()
        }
    }

    private fun handleFetchError() {
        Toast.makeText(this, getString(R.string.fail_account_activity_load), Toast.LENGTH_LONG)
            .show()
        Timber.w("Error fetching account overview")
    }

}