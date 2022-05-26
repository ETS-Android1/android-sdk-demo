package com.geomoby.demoapp.domain.usecases.geomoby

import com.geomoby.classes.GeomobyFenceView
import com.geomoby.demoapp.domain.repositories.GeomobyManager
import java.util.ArrayList

class DistanceChanged(val geomobyManager: GeomobyManager) {

    operator fun invoke(distance: String?, inside: Boolean){
        geomobyManager.distanceChanged(distance, inside)
    }
}