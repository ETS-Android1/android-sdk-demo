package com.geomoby.demoapp.logic.settings;

import com.geomoby.demoapp.GeoMobyApplication;
import com.geomoby.demoapp.data.SharedPreferences;

public class SettingsManager {
    public static final int MAP_MODE_STANDARD = 0;
    public static final int MAP_MODE_HYBRID = 1;
    public static final int MAP_MODE_SATELLITE = 2;

    private static SettingsManager mInstance = null;

    public static SettingsManager getInstance() {
        if (mInstance == null) {
            mInstance = new SettingsManager();
        }
        return mInstance;
    }

    private SettingsManager() {}

    public void setMapMode(int mapMode) {
        SharedPreferences.setMapMode(GeoMobyApplication.getContext(), mapMode);
    }

    public int getMapMode() {
        return SharedPreferences.getMapMode(GeoMobyApplication.getContext());
    }
}
