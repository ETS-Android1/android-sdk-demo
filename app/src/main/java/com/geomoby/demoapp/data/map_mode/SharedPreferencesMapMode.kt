package com.geomoby.demoapp.data.map_mode

import android.content.Context
import android.util.Log
import com.geomoby.demoapp.domain.repositories.MapMode

class SharedPreferencesMapMode(private val ctx: Context): MapMode {
    companion object{
        private const val SETTINGS_KEY = "GeoMobySettings"
        private const val MAP_MODE = "GeoMobyMapMode"
    }

    override fun setMapMode(mode: Int) {
        Log.d("SPMapMode","Map mode updated $mode")
        ctx.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE).edit().putInt(MAP_MODE, mode)
            .apply()
    }

    override fun getMapMode(): Int {
        return ctx.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE).getInt(MAP_MODE, 0)
    }
}