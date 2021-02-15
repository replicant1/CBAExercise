package com.bailey.rod.cbaexercise.viewmodel

import com.bailey.rod.cbaexercise.data.XAtm

data class MapsActivityViewModelState(
    val mapZoomLevel: Float,
    val mapCenterLatitude: Float?,
    val mapCenterLongitude: Float?,
    val atmInfoWindowShowing: Boolean,
    val atm: XAtm
)
