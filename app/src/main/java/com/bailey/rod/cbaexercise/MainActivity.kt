package com.bailey.rod.cbaexercise

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bailey.rod.cbaexercise.data.XAccountActivitySummary
import com.bailey.rod.cbaexercise.ui.TxListAdapter
import java.io.BufferedReader
import java.io.IOException

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Account details"

        val accountSummary = parseJson()
        val txListView: RecyclerView = findViewById(R.id.rv_tx_list)
        txListView.layoutManager = LinearLayoutManager(this)
        txListView.adapter = TxListAdapter(accountSummary)

        val decor = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        txListView.addItemDecoration(decor)
    }

    private fun parseJson(): XAccountActivitySummary {
        val jsonString = applicationContext.assetFileAsString("sample_account_data.json")
        println("jsonString=$jsonString")
        val summary = XAccountActivitySummary.parse(jsonString)
        println("summary=$summary")
        return summary
    }

}


/**
 * Loads a given file from the /assets folder for this app.
 *
 * @param fileName Simple file name to be loaded.
 * @return Contents of the asset file in String form or empty String
 */
@Throws(IOException::class)
fun Context.assetFileAsString(fileName: String): String {
    val bufferedReader: BufferedReader? = null

    try {
        val inputStream = this.assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        return String(buffer)
    } finally {
        bufferedReader?.close()
    }
}