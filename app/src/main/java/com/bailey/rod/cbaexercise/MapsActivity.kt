package com.bailey.rod.cbaexercise

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bailey.rod.cbaexercise.data.XAtm
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


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mAtm: XAtm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)

        val atmData = intent.getStringExtra(EXTRA_ARG_ATM)
        Timber.d("atmData = $atmData")

        try {
            mAtm = Gson().fromJson(atmData, XAtm::class.java)
            Timber.d("mAtm = $mAtm")
        } catch (ex: JsonSyntaxException) {
            Timber.w(ex)
        }

        /**
         * Note: having trouble getting view binding going with google maps fragment
         * See: ActivityMapsBinding.kt
         * Resorting to 'findFragmentById' to get hold of the SupportMapFragment for the moment.
         */
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                    BuildConfig.InitialMapZoomLevel))
            }
        }
    }

    companion object {
        const val EXTRA_ARG_ATM = "com.bailey.rod.cbaexercise.atm"
    }
}