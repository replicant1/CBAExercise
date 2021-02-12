package com.bailey.rod.cbaexercise

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bailey.rod.cbaexercise.data.XAccountActivitySummary
import com.bailey.rod.cbaexercise.databinding.ActivityMainBinding
import com.bailey.rod.cbaexercise.ui.TxListAdapter
import com.bailey.rod.cbaexercise.viewmodel.MainActivityViewModel
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
        binding.txSwipeRefresh.setOnRefreshListener { fetchData() }

        observeViewModel()

        if (viewModel.accountActivitySummary.value == null) {
            fetchData()
        }else {
            handleFetchedData(viewModel.accountActivitySummary.value)
        }
    }

    private fun observeViewModel() {
        viewModel.accountActivitySummary.observe(this, Observer { handleFetchedData(it) })
    }

    private fun fetchData() {
        Timber.i("Forcing refresh from server")
        binding.txSwipeRefresh.isRefreshing = true
        if (BuildConfig.UseLocalData) {
            viewModel.loadSyncAccountActivitySummary(this)
        } else {
            viewModel.fetchAsyncAccountActivitySummary()
        }
    }

    private fun handleFetchedData(accountSummary: XAccountActivitySummary?) {
        if (accountSummary != null) {
            binding.rvTxList.layoutManager = LinearLayoutManager(this)
            binding.rvTxList.adapter = TxListAdapter(accountSummary)
        } else {
            Toast.makeText(this, getString(R.string.fail_account_activity_load), Toast.LENGTH_LONG)
                .show()
        }
        binding.txSwipeRefresh.isRefreshing = false
    }

}