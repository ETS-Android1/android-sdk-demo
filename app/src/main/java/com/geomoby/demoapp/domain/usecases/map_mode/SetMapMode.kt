package com.geomoby.demoapp.domain.usecases.map_mode

import android.content.Context
import com.geomoby.demoapp.domain.repositories.MapMode

class SetMapMode(private val mapMode: MapMode) {
    operator fun invoke(type:Int){
        mapMode.setMapMode(type)
    }
}