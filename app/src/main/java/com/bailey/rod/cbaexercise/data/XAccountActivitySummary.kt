package com.bailey.rod.cbaexercise.data

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class XAccountActivitySummary(
    @SerializedName("account")
    val account: XAccount?,

    @SerializedName("transactions")
    val transactions: List<XAccountTransaction>?,

    @SerializedName("pending")
    val pendingTransactions: List<XAccountTransaction>?,

    @SerializedName("atms")
    val atms: List<XAtm>?
) {

    companion object {
        fun parse(jsonString: String): XAccountActivitySummary {
            val summary =  Gson().fromJson(jsonString, XAccountActivitySummary::class.java)

            println("-- Setting pending = TRUE on the following transactions --")
            for (pendingTx in summary.pendingTransactions ?: emptyList()) {
                pendingTx.pending = true
            }
            println("-- Setting pending = FALSE on the following transactions --")
            for (nonPendingTx in summary.transactions ?: emptyList()) {
                nonPendingTx.pending = false
            }
            return summary
        }
    }
}