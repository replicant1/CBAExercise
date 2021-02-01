package com.bailey.rod.cbaexercise

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bailey.rod.cbaexercise.data.XAccountActivitySummary
import com.bailey.rod.cbaexercise.ui.TxListAdapter

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = getString(R.string.activity_title_account_details)

        val accountSummary = parseJson()
        val txListView: RecyclerView = findViewById(R.id.rv_tx_list)
        txListView.layoutManager = LinearLayoutManager(this)
        txListView.adapter = TxListAdapter(accountSummary)

        val decor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        txListView.addItemDecoration(decor)
    }

    private fun parseJson(): XAccountActivitySummary {
        val jsonString = applicationContext.assetFileAsString(SAMPLE_ACCOUNT_DATA_FILE)
        val summary = XAccountActivitySummary.parse(jsonString)
        return summary
    }

    companion object {
        const val SAMPLE_ACCOUNT_DATA_FILE = "sample_account_data.json"
    }

}