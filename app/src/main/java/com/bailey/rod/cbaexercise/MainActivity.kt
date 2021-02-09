package com.bailey.rod.cbaexercise

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bailey.rod.cbaexercise.data.XAccountActivitySummary
import com.bailey.rod.cbaexercise.net.CbaService
import com.bailey.rod.cbaexercise.net.ServiceBuilder
import com.bailey.rod.cbaexercise.ui.TxListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = getString(R.string.activity_title_account_details)

        fetchJson()
    }

    private fun fetchJson() {
        val request = ServiceBuilder.buildService(CbaService::class.java)
        val call = request.getAccountActivitySummary(SERVICE_KEY, SERVICE_FILE_NAME, SERVICE_DOWNLOAD_FLAG)

        call.enqueue(object : Callback<XAccountActivitySummary> {
            override fun onResponse(
                call: Call<XAccountActivitySummary>,
                response: Response<XAccountActivitySummary>
            ) {
                if (response.isSuccessful) {
                    println("Results from onResponse: $response")
                    updateUIFromData(response.body())
                }
            }

            override fun onFailure(call: Call<XAccountActivitySummary>, t: Throwable) {
                println("Failure when loading json over network $t")
            }
        })
    }

    private fun updateUIFromData(accountSummary : XAccountActivitySummary?) {
        if (accountSummary == null)
            return

        val txListView: RecyclerView = findViewById(R.id.rv_tx_list)
        txListView.layoutManager = LinearLayoutManager(this)
        txListView.adapter = TxListAdapter(accountSummary)

        val decor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        txListView.addItemDecoration(decor)
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