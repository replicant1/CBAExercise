package com.bailey.rod.cbaexercise

import android.app.Activity
import android.os.Bundle
import com.bailey.rod.cbaexercise.data.XAtm
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class MapsActivity : Activity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var mAtm : XAtm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val atmData = intent.getStringExtra(ARG_ATM)

        println("************************")
        println("atmData = $atmData")
        println("************************")

        try {
            mAtm = Gson().fromJson(atmData, XAtm::class.java)
            println("mAtm = $mAtm")
        } catch (ex : JsonSyntaxException) {
            println(ex)
        }


        // Obtain the MapFragment and get notified when the map is ready to be used.
        val mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        if (mAtm.location != null) {
            val atmLocation = mAtm.location
            if ((atmLocation?.lat != null) && (atmLocation.lng != null)) {
                val markerLatLng = LatLng(atmLocation.lat.toDouble(), atmLocation.lng.toDouble())
                mMap.addMarker(
                    MarkerOptions()
                        .position(markerLatLng)
                        .title(mAtm.name)
                        .snippet(mAtm.address)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_atm_commbank)))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, MAP_INITIAL_ZOOM))
            }
        }
    }

    companion object {
        const val ARG_ATM = "com.bailey.rod.cbaexercise.atm"
        const val MAP_INITIAL_ZOOM = 16F
    }
}