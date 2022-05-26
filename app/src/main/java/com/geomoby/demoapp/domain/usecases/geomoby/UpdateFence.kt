package com.geomoby.demoapp.domain.usecases.geomoby

import com.geomoby.demoapp.domain.repositories.GeomobyManager

class UpdateFence(val geomobyManager: GeomobyManager) {

    operator fun invoke(){
        geomobyManager.updateFences()
    }
}