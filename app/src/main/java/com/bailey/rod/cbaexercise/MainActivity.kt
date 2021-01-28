package com.bailey.rod.cbaexercise

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bailey.rod.cbaexercise.data.XAccountActivitySummary
import java.io.BufferedReader
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        parseJson()

    }

    private fun parseJson() {
        val jsonString = applicationContext.assetFileAsString("sample_account_data.json")
        System.out.println("jsonString=$jsonString")
        val summary = XAccountActivitySummary.parse(jsonString)
        System.out.println("summary=$summary")


    }

}


/**
 * Loads a given file from the /sampledata folder for this app.
 *
 * @param context App context
 * @param assetFileName Simple file name to be loaded.
 * @return Contents of the asset file in String form
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