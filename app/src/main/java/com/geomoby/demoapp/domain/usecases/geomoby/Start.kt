package com.geomoby.demoapp.domain.usecases.geomoby

import com.geomoby.demoapp.domain.repositories.GeomobyManager

class Start(val geomobyManager: GeomobyManager) {

    operator fun invoke(){
        geomobyManager.start()
    }
}