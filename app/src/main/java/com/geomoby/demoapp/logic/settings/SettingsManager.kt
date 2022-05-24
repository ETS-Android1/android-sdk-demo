package com.geomoby.demoapp.logic.settings

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.geomoby.demoapp.data.SharedPreferences.setMapMode
import com.geomoby.demoapp.data.SharedPreferences.getMapMode
import com.geomoby.demoapp.GeoMobyApplication
import javax.inject.Singleton

class SettingsManager(val context: Context) {

    var mapMode: Int
        get() = getMapMode(context)
        set(mapMode) {
            setMapMode(context, mapMode)
        }

    companion object {
        const val MAP_MODE_STANDARD = 0
        const val MAP_MODE_HYBRID = 1
        const val MAP_MODE_SATELLITE = 2

        /*@SuppressLint("StaticFieldLeak")
        private var mInstance: SettingsManager? = null

        @JvmStatic
        fun getInstance(context: Context):SettingsManager {
                if (mInstance == null) {
                    mInstance = SettingsManager(context)
                }
                return mInstance!!
            }*/
    }
}