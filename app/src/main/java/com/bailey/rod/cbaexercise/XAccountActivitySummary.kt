package com.bailey.rod.cbaexercise

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class XAccountActivitySummary(
    @SerializedName("account")
    val account: XAccount?,

    @SerializedName("transactions")
    val transactions: List<XAccountTransaction>?,

    @SerializedName("atms")
    val atms: List<XAtm>?
) {

    companion object {
        fun parse(jsonString: String) : XAccountActivitySummary {
            return Gson().fromJson(jsonString, XAccountActivitySummary::class.java)
        }
    }
}