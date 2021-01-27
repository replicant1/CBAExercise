package com.bailey.rod.cbaexercise

import com.google.gson.annotations.SerializedName

data class XAtm(
    @SerializedName("id")
    val id : String?,

    @SerializedName("name")
    val name : String?,

    @SerializedName("address")
    val address : String?,

    @SerializedName("location")
    val location : XLocation?
)