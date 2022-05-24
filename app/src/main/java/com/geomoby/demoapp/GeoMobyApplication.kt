package com.geomoby.demoapp

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.geomoby.demoapp.GeoMobyApplication
import com.geomoby.demoapp.logic.geomoby.GeomobyStartManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GeoMobyApplication : MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()
        GeomobyStartManager().start(this)
    }
}