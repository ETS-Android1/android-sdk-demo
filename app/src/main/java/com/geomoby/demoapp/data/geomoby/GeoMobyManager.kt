package com.geomoby.demoapp.data.geomoby

import android.location.Location
import android.util.Log
import com.geomoby.GeoMoby
import com.geomoby.callbacks.GeomobyServiceCallback
import com.geomoby.classes.GeomobyError
import com.geomoby.classes.GeomobyFenceView
import com.geomoby.core.data.data_source.preference_storages.GeomobyDataStorage
import com.geomoby.demoapp.domain.repositories.GeoMobyManagerCallback
import com.geomoby.demoapp.domain.repositories.GeomobyManager
import com.geomoby.demoapp.ui.firebase.FirebaseManager
import java.util.ArrayList

class GeoMobyManager: GeomobyManager {

    private val TAG = "GeoMobyManager"
    private var mDelegate: GeoMobyManagerCallback? = null
    private var mStarted = false

    override fun setDelegate(delegate: GeoMobyManagerCallback, geomobyDataStorage: GeomobyDataStorage) {
        mDelegate = delegate
        // Initial states
        mDelegate?.let { callback ->
            val gds = geomobyDataStorage
            gds.initLocation?.let {
                callback.onInitLocationChanged(it)
            }

            val inside = gds.inside
            gds.minDistance?.let {
                callback.onDistanceChanged(it, inside)
            }

            val scanning = gds.scanning
            callback.onBeaconScanChanged(scanning)

            gds.fenceViews?.let { array->
                val arrayList = ArrayList<GeomobyFenceView>()
                array.forEach {
                    arrayList.add(it)
                }
                callback.onFenceListChanged(arrayList)
            }

        }
    }

    override fun start() {
        Log.d("Permission", "Point 3")
        //if (!mStarted) {
        GeoMoby.start()
        mStarted = true
        //}
    }

    override fun updateFences() {
        GeoMoby.updateFences()
    }

    override fun updateFirebaseId(firebaseId: String?) {
        firebaseId?.let {
            GeoMoby.updateInstanceId(it)
        }
    }

    override fun initLocationChanged(location: Location?) {
        mDelegate?.let {
            it.onInitLocationChanged(location)
        }
    }

    override fun distanceChanged(distance: String?, inside: Boolean) {
        mDelegate?.let {
            it.onDistanceChanged(distance, inside)
        }
    }

    override fun beaconScanChanged(scanning: Boolean) {
        mDelegate?.let {
            it.onBeaconScanChanged(scanning)
        }
    }

    override fun fenceListChanged(fences: ArrayList<GeomobyFenceView>?) {
        mDelegate?.let {
            it.onFenceListChanged(fences)
        }
    }

    override fun onStarted() {
        Log.d(TAG, "Service started!")
        FirebaseManager.initFirebase()
    }

    override fun onStopped() {
        Log.d(TAG, "Service stopped!")
    }

    override fun onError(geomobyError: GeomobyError?) {
        Log.d(TAG, "Error - " + geomobyError?.message + "!")
    }
}