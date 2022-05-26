package com.geomoby.demoapp.domain.repositories

import android.location.Location
import com.geomoby.callbacks.GeomobyServiceCallback
import com.geomoby.classes.GeomobyFenceView
import com.geomoby.core.data.data_source.preference_storages.GeomobyDataStorage
import java.util.ArrayList

interface GeomobyManager: GeomobyServiceCallback {
    fun setDelegate(delegate: GeoMobyManagerCallback, geomobyDataStorage: GeomobyDataStorage)
    fun start()
    fun updateFences()
    fun updateFirebaseId(firebaseId: String?)
    fun initLocationChanged(location: Location?)
    fun distanceChanged(distance: String?, inside: Boolean)
    fun beaconScanChanged(scanning: Boolean)
    fun fenceListChanged(fences: ArrayList<GeomobyFenceView>?)
}

interface GeoMobyManagerCallback {
    fun onInitLocationChanged(location: Location?)
    fun onDistanceChanged(distance: String?, inside: Boolean)
    fun onBeaconScanChanged(scanning: Boolean)
    fun onFenceListChanged(fences: ArrayList<GeomobyFenceView>?)
}
