package com.geomoby.demoapp

import com.geomoby.demoapp.logic.geomoby.GeoMobyManager.Companion.instance
import com.geomoby.demoapp.logic.system.NotificationManager.sendNotification
import com.geomoby.GeomobyUserService
import com.geomoby.demoapp.logic.geomoby.GeoMobyManager
import com.geomoby.classes.GeomobyActionBasic
import android.content.Intent
import com.geomoby.demoapp.ui.main.MainActivityNew
import com.geomoby.demoapp.R
import android.os.Build
import android.app.PendingIntent
import android.app.AlarmManager
import android.content.Context
import android.location.Location
import android.util.Log
import com.geomoby.classes.GeomobyActionData
import com.geomoby.demoapp.ui.discount.DiscountActivityNew
import com.geomoby.classes.GeomobyFenceView
import com.geomoby.demoapp.GeoService
import androidx.core.content.ContextCompat
import com.geomoby.demoapp.data.EventStorage
import com.geomoby.demoapp.data.EventStorageSP
import com.geomoby.demoapp.data.ExperimentsLogger
import java.lang.StringBuilder
import java.util.ArrayList

class GeoService : GeomobyUserService() {
    override fun beaconScan(scanning: Boolean) {
        instance!!.beaconScanChanged(scanning)
    }

    override fun geomobyActionBasic(geomobyActionBasic: GeomobyActionBasic) {
        val openIntent = Intent(this, MainActivityNew::class.java)
        sendNotification(
            this,
            openIntent,
            geomobyActionBasic.title,
            geomobyActionBasic.body,
            R.mipmap.message
        )
        EventStorageSP(applicationContext).addEvent(
            EventStorage.Event(
                title = geomobyActionBasic.title,
                message = geomobyActionBasic.body)
        )
        ExperimentsLogger(applicationContext).addEvent("${geomobyActionBasic.title}: ${geomobyActionBasic.body}")
    }

    override fun onCreate() {
        super.onCreate()
        instance!!.start()
    }

    override fun onDestroy() {
        Log.d("API", "Destroy Service")
        //GeoMoby.Companion.stop();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            val restartService = Intent(applicationContext, this.javaClass)

            val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getService(
                    applicationContext,
                    1,
                    restartService,
                    PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                PendingIntent.getService(
                    applicationContext,
                    1,
                    restartService,
                    PendingIntent.FLAG_ONE_SHOT
                )
            }
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager?.set(AlarmManager.ELAPSED_REALTIME, 5000, pendingIntent)
        }
        super.onDestroy()
    }

    override fun geomobyActionData(geomobyActionData: GeomobyActionData) {
        val key = "id"
        val value = geomobyActionData.getValue(key)
        val eventStorage = EventStorageSP(applicationContext)
        val experimentLogger = ExperimentsLogger(applicationContext)
        if (value != null) {
            val openIntent = Intent(this, DiscountActivityNew::class.java)
            openIntent.putExtra(key, value)
            val event = geomobyActionData.getValue("id")
            var icon = R.mipmap.data
            var title = "Data Action Received!"
            when (event) {
                "Enter" -> {
                    title = "Welcome to our venue"
                    icon = R.mipmap.hotel
                }
                "Exit" -> {
                    title = "Good Bye and see you again soon"
                    icon = R.mipmap.good_bye
                }
                "Drink" -> {
                    title = "Get one drink for only $5"
                    icon = R.mipmap.drink
                }
                "Offer" -> {
                    title = "Today's Special offer"
                    icon = R.mipmap.offer
                }
                "enter_geo" -> {
                    val fencesEnter = geomobyActionData.getValue("fences")
                    title = "Enter geo $fencesEnter"
                    icon = R.mipmap.hotel
                }
                "exit_geo" -> {
                    val fencesExit = geomobyActionData.getValue("fences")
                    title = "Exit geo $fencesExit"
                    icon = R.mipmap.good_bye
                }
                "dwell_geo" -> {
                    title = "Dwell geo"
                    icon = R.mipmap.drink
                }
            }
            eventStorage.addEvent(
                EventStorage.Event(
                    title = title,
                    message = title
                )
            )
            experimentLogger.addEvent(title)
            sendNotification(this, openIntent, title, title, icon)
        } else {
            sendNotification(
                this, null, "aaaaa", "bbbbb",
                R.mipmap.offer
            )
            eventStorage.addEvent(
                EventStorage.Event(
                    title = "empty",
                    message = "empty"
                )
            )
            experimentLogger.addEvent("empty log notification")
        }
    }

    override fun newFenceList(fences: ArrayList<GeomobyFenceView>) {
        val builder = StringBuilder()
        builder.append("Geofences list: \n")
        for ((name, type, _, geometries) in fences) {
            builder.append(
                """	 - name: $name type: $type geometries: $geometries
"""
            )
        }
        Log.d("GeoService", "New Fences - $builder")
        instance!!.fenceListChanged(fences)
    }

    override fun newInitLocation(location: Location) {
        instance!!.initLocationChanged(location)
        EventStorageSP(this).addEvent(
            EventStorage.Event(
                title = "Init location updated",
                message = "location - accuracy - ${location.accuracy} " +
                        "longitude - ${location.longitude} latitude - ${location.latitude}"
            )
        )

        ExperimentsLogger(applicationContext).addEvent("Init location updated: " +
                "location - accuracy - ${location.accuracy} " +
                "longitude - ${location.longitude} latitude - ${location.latitude}")
    }

    // Method for access to low level logs
    override fun newLogMessage(message: String) {
        ExperimentsLogger(applicationContext).addEvent(message)
    }

    override val notificationIntent: Intent
        get() = Intent(this, MainActivityNew::class.java)
    override val notificationSmallIconId: Int
        get() = R.mipmap.ic_launcher
    override val notificationTitle: String
        get() = "GeoMoby service is running"

    override fun newDistance(distance: String, inside: Boolean) {
        instance!!.distanceChanged(distance, inside)
    }

    companion object {
        fun setForeground(context: Context?) {
            val intent = Intent(context, GeoService::class.java)
            intent.action = ACTION_FOREGROUND
            ContextCompat.startForegroundService(context!!, intent)
        }

        fun disableForeground(context: Context?) {
            val intent = Intent(context, GeoService::class.java)
            intent.action = ACTION_SERVICE
            ContextCompat.startForegroundService(context!!, intent)
        }
    }
}