package com.geomoby.demoapp

import androidx.multidex.MultiDexApplication
import com.geomoby.demoapp.data.geomoby.GeomobyStartManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GeoMobyApplication : MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()
        GeomobyStartManager().start(this)
    }
}