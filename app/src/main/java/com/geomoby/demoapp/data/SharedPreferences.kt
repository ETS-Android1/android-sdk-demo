package com.geomoby.demoapp.data

import android.content.Context

object SharedPreferences {
    private const val SETTINGS_KEY = "GeoMobySettings"
    private const val MAP_MODE = "GeoMobyMapMode"
    @JvmStatic
    fun setMapMode(ctx: Context, mode: Int) {
        ctx.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE).edit().putInt(MAP_MODE, mode)
            .apply()
    }

    @JvmStatic
    fun getMapMode(ctx: Context): Int {
        return ctx.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE).getInt(MAP_MODE, 0)
    }
}