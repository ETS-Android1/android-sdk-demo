package com.geomoby.demoapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.geomoby.GeoMoby;
import com.geomoby.GeomobyUserService;
import com.geomoby.classes.GeomobyActionBasic;
import com.geomoby.classes.GeomobyActionData;
import com.geomoby.classes.GeomobyFenceView;
import com.geomoby.demoapp.logic.geomoby.GeoMobyManager;
import com.geomoby.demoapp.logic.system.NotificationManager;
import com.geomoby.demoapp.ui.discount.DiscountActivity;
import com.geomoby.demoapp.ui.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GeoService extends GeomobyUserService {

    @Override
    public void beaconScan(boolean scanning) {
        GeoMobyManager.getInstance().beaconScanChanged(scanning);
    }

    @Override
    public void geomobyActionBasic(GeomobyActionBasic geomobyActionBasic) {
            Intent openIntent = new Intent(this, MainActivity.class);
            NotificationManager.sendNotification(this, openIntent, geomobyActionBasic.getTitle(), geomobyActionBasic.getBody(), R.mipmap.message);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GeoMobyManager.getInstance().start();
    }

    @Override
    public void onDestroy() {
        Log.d("API","Destroy Service");
        //GeoMoby.Companion.stop();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Intent restartService = new Intent(getApplicationContext(), this.getClass());
            PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartService, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.set(AlarmManager.ELAPSED_REALTIME, 5000, pendingIntent);
            }
        }

        super.onDestroy();
    }

    @Override
    public void geomobyActionData(GeomobyActionData geomobyActionData) {
        String key = "id";
        String value = geomobyActionData.getValue(key);
        if (value != null) {
            Intent openIntent = new Intent(this, DiscountActivity.class);
            openIntent.putExtra(key, value);

            String event = geomobyActionData.getValue("id");
            int icon = R.mipmap.data;
            String title = "Data Action Received!";
            switch (event) {
                case "Enter":
                    title = "Welcome to our venue";
                    icon = R.mipmap.hotel;
                    break;
                case "Exit":
                    title = "Good Bye and see you again soon";
                    icon = R.mipmap.good_bye;
                    break;
                case "Drink":
                    title = "Get one drink for only $5";
                    icon = R.mipmap.drink;
                    break;
                case "Offer":
                    title = "Today's Special offer";
                    icon = R.mipmap.offer;
                    break;
                case "enter_geo":
                    String fencesEnter = geomobyActionData.getValue("fences");
                    title = "Enter geo "+fencesEnter;
                    icon = R.mipmap.hotel;
                    break;
                case "exit_geo":
                    String fencesExit = geomobyActionData.getValue("fences");
                    title = "Exit geo "+fencesExit;
                    icon = R.mipmap.good_bye;
                    break;
                case "dwell_geo":
                    title = "Dwell geo";
                    icon = R.mipmap.drink;
                    break;
            }

            NotificationManager.sendNotification(this, openIntent, title, title, icon);
        } else {
            NotificationManager.sendNotification(this, null, "aaaaa", "bbbbb",
                    R.mipmap.offer);

        }
    }

    @Override
    public void newFenceList(@NotNull ArrayList<GeomobyFenceView> fences) {
        StringBuilder builder = new StringBuilder();

        builder.append("Geofences list: \n");
        for(GeomobyFenceView fence: fences){
            builder.append("\t - name: "+fence.getName()+" type: "+fence.getType()+" geometries: "+fence.getGeometries()+"\n");
        }
        Log.d("GeoService","New Fences - "+builder.toString());
        GeoMobyManager.getInstance().fenceListChanged(fences);
    }

    @Override
    public void newInitLocation(@NotNull Location location) {
        GeoMobyManager.getInstance().initLocationChanged(location);
    }

    @NotNull
    @Override
    public Intent getNotificationIntent() {
        return new Intent(this, MainActivity.class);
    }

    @Override
    public Integer getNotificationSmallIconId() {
        return R.mipmap.ic_launcher;
    }

    @NotNull
    @Override
    public String getNotificationTitle() {
        return "GeoMoby service is running";
    }

    public static void setForeground(Context context) {
        Intent intent = new Intent(context, GeoService.class);
        intent.setAction(GeomobyUserService.ACTION_FOREGROUND);
        ContextCompat.startForegroundService(context, intent);
    }

    public static void disableForeground(Context context) {
        Intent intent = new Intent(context, GeoService.class);
        intent.setAction(GeomobyUserService.ACTION_SERVICE);
        ContextCompat.startForegroundService(context, intent);
    }

    @Override
    public void newDistance(@NotNull String distance, boolean inside) {
        GeoMobyManager.getInstance().distanceChanged(distance, inside);
    }
}
