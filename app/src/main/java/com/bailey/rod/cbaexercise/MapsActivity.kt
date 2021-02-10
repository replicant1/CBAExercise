package com.bailey.rod.cbaexercise

import android.app.Activity
import android.os.Bundle
import com.bailey.rod.cbaexercise.data.XAtm
import com.bailey.rod.cbaexercise.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import timber.log.Timber

/**
 * Note: having trouble getting view binding going with google maps <fragment>
 * See: ActivityMapsBinding.kt
 */
class MapsActivity : Activity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mAtm: XAtm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)

        val atmData = intent.getStringExtra(ARG_ATM)
        Timber.d("atmData = $atmData")

        try {
            mAtm = Gson().fromJson(atmData, XAtm::class.java)
            Timber.d("mAtm = $mAtm")
        } catch (ex: JsonSyntaxException) {
            Timber.w(ex)
        }

        // Obtain the MapFragment and get notified when the map is ready to be used.
        val mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in ATM location and move the camera there
        if (mAtm.location != null) {
            val atmLocation = mAtm.location
            if ((atmLocation?.lat != null) && (atmLocation.lng != null)) {
                val markerLatLng = LatLng(atmLocation.lat.toDouble(), atmLocation.lng.toDouble())
                mMap.addMarker(
                    MarkerOptions()
                        .position(markerLatLng)
                        .title(mAtm.name)
                        .snippet(mAtm.address)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_atm_commbank))
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, MAP_INITIAL_ZOOM))
            }
        }
    }

    companion object {
        const val ARG_ATM = "com.bailey.rod.cbaexercise.atm"
        const val MAP_INITIAL_ZOOM = 16F
    }
}