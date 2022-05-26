package com.geomoby.demoapp.domain.usecases.geomoby

import com.geomoby.demoapp.domain.repositories.GeomobyManager

class BeaconScanChanged(val geomobyManager: GeomobyManager) {

    operator fun invoke(scanning:Boolean){
        geomobyManager.beaconScanChanged(scanning)
    }
}