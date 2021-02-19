package com.bailey.rod.cbaexercise.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bailey.rod.cbaexercise.BuildConfig
import com.bailey.rod.cbaexercise.R
import com.bailey.rod.cbaexercise.data.XAtm
import com.bailey.rod.cbaexercise.viewmodel.AtmOnMapViewModel
import com.bailey.rod.cbaexercise.viewmodel.AtmOnMapViewModelState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import timber.log.Timber


class AtmOnMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: AtmOnMapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_atm_on_map)

        val atmData = intent.getStringExtra(EXTRA_ARG_ATM)
        Timber.d("atmData = $atmData")

        viewModel = ViewModelProvider(this).get(AtmOnMapViewModel::class.java)

        try {
            val mAtm: XAtm = Gson().fromJson(atmData, XAtm::class.java)
            // If the view model has some state info, restore that state into the view
            // Else initialise the view model's value for 'state'
            if (viewModel.state.value == null) {
                val initState = AtmOnMapViewModelState(
                    BuildConfig.InitialMapZoomLevel,
                    mAtm.location?.lat,
                    mAtm.location?.lng,
                    true, // Initial state of the info window showing
                    mAtm
                )
                viewModel.state.value = initState
            }
        } catch (ex: JsonSyntaxException) {
            Timber.w(ex)
        }

        /**
         * Note: having trouble getting view binding going with google maps fragment
         * Resorting to 'findFragmentById' to get hold of the SupportMapFragment for the moment.
         */
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setupGoogleMap()
        applyStateDataToMap()
    }

    private fun setupGoogleMap() {
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.uiSettings.isCompassEnabled = false
        mMap.uiSettings.isIndoorLevelPickerEnabled = false
        mMap.uiSettings.isMyLocationButtonEnabled = false
        mMap.uiSettings.isRotateGesturesEnabled = false
        mMap.uiSettings.isScrollGesturesEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = false
    }

    /**
     * Apply this.mState to this.mMap, if both are non-null
     */
    private fun applyStateDataToMap() {
        val safeState = viewModel.state.value
        val safeAtm = viewModel.state.value?.atm

        if ((safeState != null) && (safeAtm != null)) {
            val atmLocation = safeAtm.location
            if ((atmLocation?.lat != null) && (atmLocation.lng != null)) {

                // Create a marker for the ATM
                val atmLatLng = LatLng(atmLocation.lat.toDouble(), atmLocation.lng.toDouble())
                val marker = mMap.addMarker(
                    MarkerOptions()
                        .position(atmLatLng)
                        .title(safeAtm.name)
                        .snippet(safeAtm.address)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_atm_commbank))
                )

                // NOTE: Does not seem to be a way to listen for change in info window visibility.
                // It should be used to update atmInfoWindowShowing.

                // Set the center lat,lng of the map and zoom level
                if ((safeState.mapCenterLatitude != null) && (safeState.mapCenterLongitude != null)) {
                    val mapLatLng =
                        LatLng(safeState.mapCenterLatitude, safeState.mapCenterLongitude)
                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            mapLatLng,
                            safeState.mapZoomLevel
                        )
                    )
                }

                if (safeState.atmInfoWindowShowing == true) {
                    marker?.showInfoWindow()
                } else {
                    marker?.hideInfoWindow()
                }
            }
        }

        mMap.setOnCameraMoveListener {
            val cameraPos = mMap.cameraPosition

            println("Into onCameraMoveListener")
            println("latitude= ${mMap.cameraPosition.target.latitude}")
            println("longitude = ${mMap.cameraPosition.target.longitude}")
            println("zoom = ${mMap.cameraPosition.zoom}")

            // Only latitude, longitude and zoom changes are notified
            val newState = AtmOnMapViewModelState(
                cameraPos.zoom,
                cameraPos.target.latitude,
                cameraPos.target.longitude,
                viewModel.state.value?.atmInfoWindowShowing,
                viewModel.state.value?.atm
            )
            Timber.d("newState = $newState")
            viewModel.state.value = newState
        }


    }

    companion object {
        const val EXTRA_ARG_ATM = "com.bailey.rod.cbaexercise.atm"
    }
}