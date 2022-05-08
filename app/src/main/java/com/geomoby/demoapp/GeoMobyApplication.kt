package com.geomoby.demoapp

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.geomoby.demoapp.GeoMobyApplication

class GeoMobyApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        var context: Context? = null
            private set
    }
}