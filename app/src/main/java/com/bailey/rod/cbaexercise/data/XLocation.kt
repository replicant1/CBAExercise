package com.bailey.rod.cbaexercise.data

import com.google.gson.annotations.SerializedName

data class XLocation(
    @SerializedName("lat")
    val lat: Double?,

    @SerializedName("lng")
    val lng: Double?
)