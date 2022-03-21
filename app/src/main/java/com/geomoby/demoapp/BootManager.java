package com.geomoby.demoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;


public class BootManager extends BroadcastReceiver {
    public BootManager() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Starting service when boot up
        Intent serviceIntent = new Intent(context, GeoService.class);
        ContextCompat.startForegroundService(context, serviceIntent);
    }
}
