package com.geomoby.demoapp.logic.geomoby

import android.content.Context
import android.location.Location
import android.util.Log
import com.geomoby.GeoMoby
import com.geomoby.GeoMoby.Companion.setTags
import com.geomoby.GeoMoby.Companion.updateInstanceId
import com.geomoby.callbacks.GeomobyServiceCallback
import com.geomoby.demoapp.logic.firebase.FirebaseManager.Companion.initFirebase
import com.geomoby.core.data.data_source.preference_storages.GeomobyDataStorage
import com.geomoby.classes.GeomobyFenceView
import com.geomoby.classes.GeomobyError
import java.util.*

class GeomobyStartManager : GeomobyServiceCallback {
    fun start(context: Context){
        // Build geomoby. build() method returns Geomoby object
        GeoMoby.Builder(context, "OCSQSV3P", this)
            .setDevMode(true)
            .setUUID("f7826da6-4fa2-4e98-8024-bc5b71e0893e")
            .setOfflineMode(true)
            .setSilenceWindow(23, 0)
            .build()
        val tags: MutableMap<String?, String?> = HashMap()
        tags["gender"] = "male"
        tags["age"] = "27"
        tags["membership"] = "gold"
        setTags(tags)
    }

    override fun onError(error: GeomobyError?) {
        TODO("Not yet implemented")
    }

    override fun onStarted() {
        TODO("Not yet implemented")
        initFirebase()
    }

    override fun onStopped() {
        TODO("Not yet implemented")
    }
}

class GeoMobyManager private constructor(): GeomobyServiceCallback {
    private val TAG = GeoMobyManager::class.java.simpleName
    private var mDelegate: GeoMobyManagerCallback? = null
    private var mStarted = false

    fun setDelegate(delegate: GeoMobyManagerCallback, geomobyDataStorage: GeomobyDataStorage) {
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

    fun start() {
        Log.d("Permission", "Point 3")
        //if (!mStarted) {
        GeoMoby.start()
        mStarted = true
        //}
    }

    fun updateFences() {
        GeoMoby.updateFences()
    }

    fun updateFirebaseId(firebaseId: String?) {
        updateInstanceId(firebaseId!!)
    }

    fun initLocationChanged(location: Location?) {
        mDelegate?.let {
            it.onInitLocationChanged(location)
        }
    }

    fun distanceChanged(distance: String?, inside: Boolean) {
        mDelegate?.let {
            it.onDistanceChanged(distance, inside)
        }
    }

    fun beaconScanChanged(scanning: Boolean) {
        mDelegate?.let {
            it.onBeaconScanChanged(scanning)
        }
    }

    fun fenceListChanged(fences: ArrayList<GeomobyFenceView>?) {
        mDelegate?.let {
            mDelegate!!.onFenceListChanged(fences)
        }
    }

    override fun onStarted() {
        Log.d(TAG, "Service started!")
        initFirebase()
    }

    override fun onStopped() {
        Log.d(TAG, "Service stopped!")
    }

    override fun onError(geomobyError: GeomobyError?) {
        Log.d(TAG, "Error - " + geomobyError!!.message + "!")
    }

    interface GeoMobyManagerCallback {
        fun onInitLocationChanged(location: Location?)
        fun onDistanceChanged(distance: String?, inside: Boolean)
        fun onBeaconScanChanged(scanning: Boolean)
        fun onFenceListChanged(fences: ArrayList<GeomobyFenceView>?)
    }

    companion object {
        private var mInstance: GeoMobyManager? = null

        @JvmStatic
        fun getInstance():GeoMobyManager {
                if (mInstance == null) {
                    mInstance = GeoMobyManager()
                }
                return mInstance!!
            }
    }
}