package com.geomoby.demoapp.domain.repositories

import android.location.Location

interface LocationManager {
    fun setDelegate(delegate: LocationManagerCallback?)
}

interface LocationManagerCallback {
    fun onLocationChanged(location: Location?)
}