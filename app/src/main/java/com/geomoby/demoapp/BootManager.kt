package com.geomoby.demoapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.geomoby.demoapp.GeoService

class BootManager : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Starting service when boot up
        val serviceIntent = Intent(context, GeoService::class.java)
        //ContextCompat.startForegroundService(context, serviceIntent);
        context.startService(serviceIntent)
    }
}