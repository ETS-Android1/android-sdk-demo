package com.geomoby.demoapp.domain.usecases.location_manager

import com.geomoby.demoapp.domain.repositories.LocationManager
import com.geomoby.demoapp.domain.repositories.LocationManagerCallback

class SetDelegate(val locationManager: LocationManager) {

    operator fun invoke(delegate: LocationManagerCallback?){
        locationManager.setDelegate(delegate)
    }
}