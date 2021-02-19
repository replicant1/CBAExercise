package com.bailey.rod.cbaexercise.viewmodel

import com.bailey.rod.cbaexercise.data.XAtm

/**
 * View model state for the AtmOnMapActivity view.
 */
data class AtmOnMapViewModelState(
    val mapZoomLevel: Float,
    val mapCenterLatitude: Double?,
    val mapCenterLongitude: Double?,
    val atmInfoWindowShowing: Boolean?,
    val atm: XAtm?
)
