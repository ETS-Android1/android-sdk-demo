package com.geomoby.demoapp.logic.system;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationManager {
    private static final String NOTIFICATION_CHANNEL_ID = "GeoMobyNotificationChannelID";
    private static final String NOTIFICATION_CHANNEL_NAME = "GeoMobyNotificationChannelName";
    private static final int NOTIFICATION_IMPRTANCE = android.app.NotificationManager.IMPORTANCE_HIGH;


    public static void sendNotification(Context context, Intent intent, String title, String body, @DrawableRes int icon) {

        TaskStackBuilder stackBuilder = TaskStackBuilder
                .create(context)
                .addNextIntentWithParentStack(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder = new Notification.Builder(context)
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setTicker(title)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
        }

        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            // For Android 8+
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NOTIFICATION_IMPRTANCE);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{500, 500, 500});
                notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            notificationManager.notify(NotificationID.getID(), notificationBuilder.build());
        }
    }

    // Use this class to generate unique notification ID
    private static class NotificationID {
        private static final AtomicInteger c = new AtomicInteger(0);
        static int getID() {
            return c.incrementAndGet();
        }
    }
}
