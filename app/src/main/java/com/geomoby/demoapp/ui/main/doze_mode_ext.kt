package com.geomoby.demoapp.ui.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.work.*
import com.geomoby.demoapp.GeoService
import java.util.concurrent.TimeUnit

const val INTERVAL_CHECK_SERVICE = 20*60_000L // 20 min
//const val INTERVAL_CHECK_SERVICE = 60_000L // 1 min
const val START_DELAY = 10_000 // 10 sec

internal fun startAlarm(context: Context) {
    val intent = Intent(context, ServiceCheck::class.java).let { intent ->
        if(Build.VERSION.SDK_INT  >= Build.VERSION_CODES.S){
            PendingIntent.getBroadcast(context, 56, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            PendingIntent.getBroadcast(context, 56, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
    val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    am.setRepeating(
        AlarmManager.RTC_WAKEUP,
        System.currentTimeMillis() + START_DELAY,
        INTERVAL_CHECK_SERVICE,
        intent
    )
}

class ServiceCheck:BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("ServiceCheck","Received Alarm")
        val serviceIntent = Intent(context, GeoService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else{
            context.startService(serviceIntent)
        }
    }
}