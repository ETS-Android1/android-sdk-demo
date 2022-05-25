package com.geomoby.demoapp.domain.repositories

import android.content.Context

interface MapMode{
    fun setMapMode(mode: Int)
    fun getMapMode():Int

    companion object{
        const val MAP_MODE_STANDARD = 0
        const val MAP_MODE_HYBRID = 1
        const val MAP_MODE_SATELLITE = 2
    }
}