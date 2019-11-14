package com.geomoby.demoapp;

import android.content.Context;
import android.content.IntentFilter;

import androidx.multidex.MultiDexApplication;

import com.geomoby.demoapp.logic.geomoby.GeoMobyStateReceiver;

public class GeoMobyApplication extends MultiDexApplication {

    private static Context mContext;
    private GeoMobyStateReceiver geoMobyStateReceiver = new GeoMobyStateReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        IntentFilter filter = new IntentFilter();
        filter.addAction("BroadcastInitLocation");
        filter.addAction("GEOMOBY_NEW_DISTANCE");
        filter.addAction("GEOMOBY_BEACON_SCAN");
        filter.addAction("GEOMOBY_NEW_FENCE_LIST");

        getApplicationContext().registerReceiver(geoMobyStateReceiver, filter);
    }

    public static Context getContext() {
        return mContext;
    }
}
