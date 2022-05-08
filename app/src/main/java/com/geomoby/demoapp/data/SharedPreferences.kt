package com.geomoby.demoapp.data;

import android.content.Context;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferences {
    private static final String SETTINGS_KEY = "GeoMobySettings";
    private static final String MAP_MODE = "GeoMobyMapMode";


    public static void setMapMode(Context ctx, int mode) {
        ctx.getSharedPreferences(SETTINGS_KEY, MODE_PRIVATE).edit().putInt(MAP_MODE, mode).apply();
    }

    public static int getMapMode(Context ctx) {
        return ctx.getSharedPreferences(SETTINGS_KEY, MODE_PRIVATE).getInt(MAP_MODE, 0);
    }
}
