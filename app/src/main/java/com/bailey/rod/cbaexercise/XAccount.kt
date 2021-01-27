package com.bailey.rod.cbaexercise

import com.google.gson.annotations.SerializedName

data class XAccount(
    @SerializedName("accountName")
    val accountName: String?,

    @SerializedName("accountNumber")
    val accountNumber: String?,

    @SerializedName("available")
    val available: Float?,

    @SerializedName("balance")
    val balance: Float?) {


}