package com.geomoby.demoapp.logic.settings

import com.geomoby.demoapp.data.SharedPreferences.setMapMode
import com.geomoby.demoapp.data.SharedPreferences.getMapMode
import com.geomoby.demoapp.GeoMobyApplication

class SettingsManager private constructor() {

    var mapMode: Int
        get() = getMapMode(GeoMobyApplication.context!!)
        set(mapMode) {
            setMapMode(GeoMobyApplication.context!!, mapMode)
        }

    companion object {
        const val MAP_MODE_STANDARD = 0
        const val MAP_MODE_HYBRID = 1
        const val MAP_MODE_SATELLITE = 2
        private var mInstance: SettingsManager? = null

        @JvmStatic
        val instance: SettingsManager?
            get() {
                if (mInstance == null) {
                    mInstance = SettingsManager()
                }
                return mInstance
            }
    }
}