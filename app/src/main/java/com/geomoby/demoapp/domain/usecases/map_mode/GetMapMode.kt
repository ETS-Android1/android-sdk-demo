package com.geomoby.demoapp.domain.usecases.map_mode

import android.content.Context
import com.geomoby.demoapp.domain.repositories.MapMode

class GetMapMode(private val mapMode: MapMode) {
    operator fun invoke():Int{
        return mapMode.getMapMode()
    }
}