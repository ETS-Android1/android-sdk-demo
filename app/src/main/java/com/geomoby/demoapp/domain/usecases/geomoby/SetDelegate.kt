package com.geomoby.demoapp.domain.usecases.geomoby

import com.geomoby.core.data.data_source.preference_storages.GeomobyDataStorage
import com.geomoby.demoapp.domain.repositories.GeoMobyManagerCallback
import com.geomoby.demoapp.domain.repositories.GeomobyManager

class SetDelegate(val geomobyManager: GeomobyManager) {

    operator fun invoke(geoMobyManagerCallback: GeoMobyManagerCallback,
                        geomobyDataStorage: GeomobyDataStorage){
        geomobyManager.setDelegate(geoMobyManagerCallback, geomobyDataStorage)
    }
}