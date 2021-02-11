package com.bailey.rod.cbaexercise

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bailey.rod.cbaexercise.data.XAccountActivitySummary
import com.bailey.rod.cbaexercise.databinding.ActivityMainBinding
import com.bailey.rod.cbaexercise.ui.TxListAdapter
import com.bailey.rod.cbaexercise.viewmodel.MainActivityViewModel

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
        fetchData()
    }

    private fun observeViewModel() {
        viewModel.accountActivitySummary.observe(this, Observer { handleFetchedData(it) })
    }

    private fun fetchData() {
        binding.txSwipeRefresh.isRefreshing = true
        viewModel.fetchAccountActivitySummary()
    }

    /**
     * Apply account activity data to RecyclerView
     */
    private fun handleFetchedData(accountSummary: XAccountActivitySummary?) {
        if (accountSummary != null) {
            binding.rvTxList.layoutManager = LinearLayoutManager(this)
            binding.rvTxList.adapter = TxListAdapter(accountSummary)
        } else {
            // TODO Show failure message in Toast
        }
        binding.txSwipeRefresh.isRefreshing = false
    }

}