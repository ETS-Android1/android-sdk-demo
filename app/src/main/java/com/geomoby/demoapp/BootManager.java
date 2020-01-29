package com.geomoby.demoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.geomoby.services.GeomobyService;

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
