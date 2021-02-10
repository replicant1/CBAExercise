package com.bailey.rod.cbaexercise

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bailey.rod.cbaexercise.data.XAccountActivitySummary
import com.bailey.rod.cbaexercise.databinding.ActivityMainBinding
import com.bailey.rod.cbaexercise.net.CbaService
import com.bailey.rod.cbaexercise.net.ServiceBuilder
import com.bailey.rod.cbaexercise.ui.TxListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.txSwipeRefresh.setOnRefreshListener { fetchJson() }
        binding.txSwipeRefresh.isRefreshing = true

        // Initialise "Timber" for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        fetchJson()
    }

    /**
     * Async fetch account activity data over network. Publish results to screen, errors to Toast.
     */
    private fun fetchJson() {
        val request = ServiceBuilder.buildService(CbaService::class.java)
        val call =
            request.getAccountActivitySummary(SERVICE_KEY, SERVICE_FILE_NAME, SERVICE_DOWNLOAD_FLAG)

        Timber.d("Fetching account activity data")

        call.enqueue(object : Callback<XAccountActivitySummary> {
            override fun onResponse(
                call: Call<XAccountActivitySummary>,
                response: Response<XAccountActivitySummary>
            ) {
                binding.txSwipeRefresh.isRefreshing = false

                if (response.isSuccessful) {
                    Timber.d("Response from server: $response")
                    val summary: XAccountActivitySummary? = response.body()
                    if (summary != null) {
                        updateUIFromData(summary)
                    }
                } else {
                    showFailToast()
                }
            }

            override fun onFailure(call: Call<XAccountActivitySummary>, t: Throwable) {
                Timber.w(t, "Failed to load account activity data")
                binding.txSwipeRefresh.isRefreshing = false
                showFailToast()
            }

            private fun showFailToast() {
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.fail_account_activity_load),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    /**
     * Apply account activity data to RecyclerView
     */
    private fun updateUIFromData(accountSummary: XAccountActivitySummary) {
        binding.rvTxList.layoutManager = LinearLayoutManager(this)
        binding.rvTxList.adapter = TxListAdapter(accountSummary)
    }

    private fun parseJson(): XAccountActivitySummary {
        val jsonString = applicationContext.assetFileAsString(SAMPLE_ACCOUNT_DATA_FILE)
        return XAccountActivitySummary.parse(jsonString)
    }

    companion object {
        const val SAMPLE_ACCOUNT_DATA_FILE = "sample_account_data.json"
        const val SERVICE_KEY = "tewg9b71x0wrou9"
        const val SERVICE_FILE_NAME = "data.json"
        const val SERVICE_DOWNLOAD_FLAG = 1
    }

}