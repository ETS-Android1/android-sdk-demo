package com.geomoby.demoapp.domain.usecases.geomoby

import com.geomoby.classes.GeomobyFenceView
import com.geomoby.core.data.data_source.preference_storages.GeomobyDataStorage
import com.geomoby.demoapp.domain.repositories.GeoMobyManagerCallback
import com.geomoby.demoapp.domain.repositories.GeomobyManager
import java.util.ArrayList

class FenceListChanged(val geomobyManager: GeomobyManager) {

    operator fun invoke(fences: ArrayList<GeomobyFenceView>?){
        geomobyManager.fenceListChanged(fences)
    }
}