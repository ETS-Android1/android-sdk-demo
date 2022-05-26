package com.geomoby.demoapp.domain.usecases.geomoby

import android.location.Location
import com.geomoby.core.data.data_source.preference_storages.GeomobyDataStorage
import com.geomoby.demoapp.domain.repositories.GeoMobyManagerCallback
import com.geomoby.demoapp.domain.repositories.GeomobyManager

class InitLocationChanged(val geomobyManager: GeomobyManager) {

    operator fun invoke(location: Location?){
        geomobyManager.initLocationChanged(location)
    }
}