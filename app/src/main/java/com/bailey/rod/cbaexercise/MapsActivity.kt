package com.bailey.rod.cbaexercise

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bailey.rod.cbaexercise.data.XAtm
import com.bailey.rod.cbaexercise.viewmodel.MapsActivityViewModelState
import com.bailey.rod.cbaexercise.viewmodel.MapsActivityViewModel
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
    private lateinit var mState: MapsActivityViewModelState
    private lateinit var viewModel: MapsActivityViewModel
    private var mStateIsReady: Boolean = false
    private var mMapIsReady: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)

        val atmData = intent.getStringExtra(EXTRA_ARG_ATM)
        Timber.d("atmData = $atmData")

        viewModel = ViewModelProvider(this).get(MapsActivityViewModel::class.java)

        observeViewModel()

        try {
            val mAtm: XAtm = Gson().fromJson(atmData, XAtm::class.java)
            if (viewModel.state.value == null) {
                val initState = MapsActivityViewModelState(
                    BuildConfig.InitialMapZoomLevel,
                    mAtm.location?.lat,
                    mAtm.location?.lng,
                    true,
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
        mMapIsReady = true
        applyStateDataToMap()
    }

    private fun observeViewModel() {
        viewModel.state.observe(this, Observer { onMapsActivityStateChange(it) })
    }

    private fun onMapsActivityStateChange(newState: MapsActivityViewModelState) {
        mState = newState
        mStateIsReady = true
        applyStateDataToMap()
    }

    /**
     * Apply this.mState to this.mMap, if both are non-null
     */
    private fun applyStateDataToMap() {
        if (mStateIsReady && mMapIsReady) {
            if (mState.atm.location != null) {
                val atmLocation = mState.atm.location
                if ((atmLocation?.lat != null) && (atmLocation.lng != null)) {
                    val atmLatLng = LatLng(atmLocation.lat.toDouble(), atmLocation.lng.toDouble())
                    val marker = mMap.addMarker(
                        MarkerOptions()
                            .position(atmLatLng)
                            .title(mState.atm.name)
                            .snippet(mState.atm.address)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_atm_commbank))
                    )

                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            atmLatLng,
                            mState.mapZoomLevel
                        )
                    )
                    if (mState.atmInfoWindowShowing) {
                        marker?.showInfoWindow()
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_ARG_ATM = "com.bailey.rod.cbaexercise.atm"
    }
}