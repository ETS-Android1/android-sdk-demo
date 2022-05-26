package com.geomoby.demoapp

import com.geomoby.GeomobyUserService
import com.geomoby.classes.GeomobyActionBasic
import android.content.Intent
import com.geomoby.demoapp.ui.main.MainActivityNew
import android.os.Build
import android.app.PendingIntent
import android.app.AlarmManager
import android.content.Context
import android.location.Location
import android.util.Log
import com.geomoby.classes.GeomobyActionData
import com.geomoby.demoapp.ui.discount.DiscountActivityNew
import com.geomoby.classes.GeomobyFenceView
import androidx.core.content.ContextCompat
import com.geomoby.demoapp.data.event_logger.EventLoggerFile
import com.geomoby.demoapp.domain.repositories.EventStorage
import com.geomoby.demoapp.domain.usecases.event_storage.EventStorageUseCases
import com.geomoby.demoapp.domain.usecases.geomoby.GeoMobyUseCases
import com.geomoby.demoapp.domain.usecases.logger.EventLoggerUseCases
import com.geomoby.demoapp.domain.usecases.notifications.NotificationsUseCase
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class GeoService: GeomobyUserService() {

    @Inject
    lateinit var geomobyUseCases: GeoMobyUseCases
    @Inject
    lateinit var notificationsUseCase: NotificationsUseCase
    @Inject
    lateinit var eventStorageUseCase: EventStorageUseCases
    @Inject
    lateinit var eventLoggerUseCases: EventLoggerUseCases

    override fun beaconScan(scanning: Boolean) {
        geomobyUseCases.beaconScanChanged(scanning)
    }

    override fun geomobyActionBasic(geomobyActionBasic: GeomobyActionBasic) {
        val openIntent = Intent(this, MainActivityNew::class.java)
        notificationsUseCase.sendNotification(
            this,
            openIntent,
            geomobyActionBasic.title,
            geomobyActionBasic.body,
            R.mipmap.message
        )
        eventStorageUseCase.addEvent(
            EventStorage.Event(
                title = geomobyActionBasic.title,
                message = geomobyActionBasic.body)
        )
        eventLoggerUseCases.addEvent("${geomobyActionBasic.title}: ${geomobyActionBasic.body}")
    }

    override fun onCreate() {
        super.onCreate()
        geomobyUseCases.start()
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
            eventStorageUseCase.addEvent(
                EventStorage.Event(
                    title = title,
                    message = title
                )
            )
            eventLoggerUseCases.addEvent(title)
            notificationsUseCase.sendNotification(this, openIntent, title, title, icon)
        } else {
            notificationsUseCase.sendNotification(
                this, null, "aaaaa", "bbbbb",
                R.mipmap.offer
            )
            eventStorageUseCase.addEvent(
                EventStorage.Event(
                    title = "empty",
                    message = "empty"
                )
            )
            eventLoggerUseCases.addEvent("empty log notification")
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
        geomobyUseCases.fenceListChanged(fences)
    }

    override fun newInitLocation(location: Location) {
        geomobyUseCases.initLocationChanged(location)
        eventStorageUseCase.addEvent(
            EventStorage.Event(
                title = "Init location updated",
                message = "location - accuracy - ${location.accuracy} " +
                        "longitude - ${location.longitude} latitude - ${location.latitude}"
            )
        )

        eventLoggerUseCases.addEvent("Init location updated: " +
                "location - accuracy - ${location.accuracy} " +
                "longitude - ${location.longitude} latitude - ${location.latitude}")
    }

    // Method for access to low level logs
    override fun newLogMessage(message: String) {
        EventLoggerFile(applicationContext).addEvent(message)
    }

    override val notificationIntent: Intent
        get() = Intent(this, MainActivityNew::class.java)
    override val notificationSmallIconId: Int
        get() = R.mipmap.ic_launcher
    override val notificationTitle: String
        get() = "GeoMoby service is running"

    override fun newDistance(distance: String, inside: Boolean) {
        geomobyUseCases.distanceChanged(distance, inside)
    }

    companion object {
        fun setForeground(context: Context?) {
            context?.let {
                val intent = Intent(it, GeoService::class.java)
                intent.action = ACTION_FOREGROUND
                ContextCompat.startForegroundService(it, intent)
            }
        }

        fun disableForeground(context: Context?) {
            context?.let {
                val intent = Intent(it, GeoService::class.java)
                intent.action = ACTION_SERVICE
                ContextCompat.startForegroundService(it, intent)
            }
        }
    }
}