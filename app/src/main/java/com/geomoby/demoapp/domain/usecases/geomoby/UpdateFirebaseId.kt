package com.geomoby.demoapp.domain.usecases.geomoby

import com.geomoby.demoapp.domain.repositories.GeomobyManager

class UpdateFirebaseId(val geomobyManager: GeomobyManager) {

    operator fun invoke(firebaseId: String){
        geomobyManager.updateFirebaseId(firebaseId)
    }
}