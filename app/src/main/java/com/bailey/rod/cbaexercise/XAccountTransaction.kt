package com.bailey.rod.cbaexercise

import com.google.gson.annotations.SerializedName

data class XAccountTransaction(
    @SerializedName("id")
    val txId: String?,

    @SerializedName("effectiveDate")
    val effectiveDate: String?,

    @SerializedName("description")

    val description: String?,

    @SerializedName("amount")
    val amount: Float?,

    @SerializedName("atmId")
    val atmId: String?,

    @SerializedName("pending")
    val pending: Boolean = false
) {}