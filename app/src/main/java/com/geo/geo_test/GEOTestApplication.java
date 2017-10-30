package com.geo.geo_test;

import android.app.Application;
import android.support.multidex.MultiDex;

public class GEOTestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
