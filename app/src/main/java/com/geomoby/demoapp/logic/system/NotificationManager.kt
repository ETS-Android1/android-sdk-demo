package com.geomoby.demoapp.logic.system

import android.content.Intent
import androidx.annotation.DrawableRes
import android.app.PendingIntent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TaskStackBuilder
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.geomoby.demoapp.logic.system.NotificationManager.NotificationID
import java.util.concurrent.atomic.AtomicInteger

object NotificationManager {
    private const val NOTIFICATION_CHANNEL_ID = "GeoMobyNotificationChannelID"
    private const val NOTIFICATION_CHANNEL_NAME = "GM Notification Manager"
    private const val NOTIFICATION_IMPRTANCE = NotificationManager.IMPORTANCE_HIGH
    @JvmStatic
    fun sendNotification(
        context: Context,
        intent: Intent?,
        title: String?,
        body: String?,
        @DrawableRes icon: Int
    ) {
        val stackBuilder = TaskStackBuilder
            .create(context)
            .addNextIntentWithParentStack(intent)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            } else {
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }

        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(body)
            .setTicker(title)
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .setTimeoutAfter(60_000)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
        }
        (context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager)?.let { notificationManager ->
            // For Android 8+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME, NOTIFICATION_IMPRTANCE
                )
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.RED
                notificationChannel.enableVibration(true)
                notificationChannel.vibrationPattern = longArrayOf(500, 500, 500)
                notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
                notificationManager.createNotificationChannel(notificationChannel)
            }
            notificationManager.notify(NotificationID.iD, notificationBuilder.build())
        }
    }

    // Use this class to generate unique notification ID
    private object NotificationID {
        private val c = AtomicInteger(2)
        val iD: Int
            get() = c.incrementAndGet()
    }
}