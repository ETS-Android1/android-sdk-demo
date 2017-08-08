package com.geo.geo_test;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;

import com.geomoby.GeoMoby;
import com.geomoby.classes.GeomobyActionBasic;
import com.geomoby.classes.GeomobyActionData;

import java.util.concurrent.atomic.AtomicInteger;

public class CallBacksReceiver extends BroadcastReceiver {
    public CallBacksReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String name;

        switch(action)
        {
            case GeoMoby.GeomobyEventEnter:
                name = intent.getExtras().getString(GeoMoby.GeomobyLocationName);
                // It is not unusual to receive multiple event notifications as the GeoMoby SDK adjusts itself to the environmental variables
                // Please use for DEBUG only
                //sendNotification(context, NotificationID.getID(), "Entered", "Entered location - " + name, R.mipmap.entry);
                break;

            case GeoMoby.GeomobyEventDwell:
                name = intent.getExtras().getString(GeoMoby.GeomobyLocationName);
                // It is not unusual to receive multiple event notifications as the GeoMoby SDK adjusts itself to the environmental variables
                // Please use for DEBUG only
                //sendNotification(context, NotificationID.getID(), "Dwelled", "Dwelled in location - " + name, R.mipmap.dwell);
                break;

            case GeoMoby.GeomobyEventExit:
                name = intent.getExtras().getString(GeoMoby.GeomobyLocationName);
                // It is not unusual to receive multiple event notifications as the GeoMoby SDK adjusts itself to the environmental variables
                // Please use for DEBUG only
                //sendNotification(context, NotificationID.getID(), "Exited", "Exited location - " + name, R.mipmap.exit);
                break;

            case GeoMoby.GeomobyActionData:
                GeomobyActionData geomobyActionData = (GeomobyActionData)intent.getSerializableExtra(GeoMoby.GeomobyActionDataContent);
                if (geomobyActionData != null) {
                    String key1 = geomobyActionData.getValue("Key1");
                    String key2 = geomobyActionData.getValue("Key2");
                    if (key1 != null && key2 != null) {
                        sendNotification(context, NotificationID.getID(), key1, key2, R.mipmap.data);
                    }
                }
                break;

            case GeoMoby.GeomobyActionBasic:
                GeomobyActionBasic geomobyActionBasic = (GeomobyActionBasic)intent.getSerializableExtra(GeoMoby.GeomobyActionBasicContent);
                if (geomobyActionBasic != null) {
                    sendNotification(context, NotificationID.getID(), geomobyActionBasic.getTitle(), geomobyActionBasic.getBody(), R.mipmap.message);
                }
                break;
        }
    }


    private void sendNotification(Context context, int id, String title, String body, @DrawableRes int icon) {

        Notification notification = new Notification.Builder(context)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(body)
                .setTicker(title)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .build();

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(id, notification);
    }

    // Use this class to generate unique notification ID
    public static class NotificationID {
        private static final  AtomicInteger c = new AtomicInteger(0);
        public static int getID() {
            return c.incrementAndGet();
        }
    }
}
