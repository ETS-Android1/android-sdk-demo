package com.geomoby.demoapp;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class GeoMobyApplication extends MultiDexApplication {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
