package com.geomoby.demoapp.logic.location

import android.Manifest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import androidx.core.app.ActivityCompat
import com.geomoby.demoapp.GeoMobyApplication
import android.content.pm.PackageManager
import android.location.Location
import com.google.android.gms.location.LocationServices
import android.os.Looper
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.LocationResult

class LocationManager private constructor() : LocationCallback(),
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private var mDelegate: LocationManagerCallback? = null

    /**
     * Google API client
     */
    private val mGoogleApiClient = ThreadLocal<GoogleApiClient>()
    fun setDelegate(delegate: LocationManagerCallback?) {
        mDelegate = delegate
    }

    private val locationRequest: LocationRequest
        private get() {
            val locationRequest = LocationRequest()
            locationRequest.priority = LocationRequest.PRIORITY_NO_POWER
            locationRequest.interval = UPDATE_INTERVAL_MILLISECONDS
            locationRequest.fastestInterval = FASTEST_INTERVAL_MILLISECONDS
            return locationRequest
        }

    private fun startLocationUpdates() {
        GeoMobyApplication.context?.let { context->
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val looper = Looper.myLooper()
                if(looper!=null){
                    LocationServices.getFusedLocationProviderClient(context)
                        .requestLocationUpdates(
                            locationRequest, this, looper
                        )
                } else {
                    Log.d("New service","looper is null")
                }
            }
        }
    }

    override fun onConnected(bundle: Bundle?) {
        startLocationUpdates()
    }

    override fun onConnectionSuspended(i: Int) {
        mGoogleApiClient.get()!!.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        mGoogleApiClient.get()!!.connect()
    }

    override fun onLocationResult(locationResult: LocationResult) {
        if (mDelegate != null) {
            mDelegate!!.onLocationChanged(locationResult.lastLocation)
        }
    }

    interface LocationManagerCallback {
        fun onLocationChanged(location: Location?)
    }

    companion object {
        private const val UPDATE_INTERVAL_MILLISECONDS = (10 * 1000).toLong()
        private const val FASTEST_INTERVAL_MILLISECONDS = (2 * 1000).toLong()
        private var mInstance: LocationManager? = null

        @JvmStatic
        val instance: LocationManager?
            get() {
                if (mInstance == null) {
                    mInstance = LocationManager()
                }
                return mInstance
            }
    }

    init {
        GeoMobyApplication.context?.let { context ->
            mGoogleApiClient.set(
                GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build()
            )
            mGoogleApiClient.get()?.connect()
        }
    }
}