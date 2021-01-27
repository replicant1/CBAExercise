package com.bailey.rod.cbaexercise

import com.google.gson.annotations.SerializedName

data class XLocation(
    @SerializedName("lat")
    val lat: Float?,

    @SerializedName("lng")
    val lng: Float?
)