package com.geomoby.demoapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.geomoby.GeoMoby;
import com.geomoby.classes.GeomobyActionBasic;
import com.geomoby.classes.GeomobyActionData;
import com.geomoby.classes.GeomobyFenceView;
import com.geomoby.demoapp.logic.geomoby.GeoMobyManager;
import com.geomoby.demoapp.ui.discount.DiscountActivity;
import com.geomoby.demoapp.ui.main.MainActivity;
import com.geomoby.managers.GeomobyDataManager;
import com.geomoby.managers.GeomobyGPSManager;
import com.geomoby.services.GeomobyService;

import java.util.ArrayList;

public class GeoService extends Service {

    public static final String CHANNEL_ID = "Geomoby update location";

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) return;

            switch (action) {
                case GeomobyGPSManager.NEW_INIT_LOCATION:
                    Location location = GeomobyDataManager.getInstance().getInitLocation();
                    GeoMobyManager.getInstance().initLocationChanged(location);
                    break;

                case GeomobyService.NEW_DISTANCE:
                    String distance = intent.getExtras().getString("distance");
                    boolean inside = intent.getExtras().getBoolean("inside");
                    GeoMobyManager.getInstance().distanceChanged(distance, inside);
                    break;

                case GeomobyService.BEACON_SCAN:
                    boolean scanning = intent.getExtras().getBoolean("scanning");
                    GeoMobyManager.getInstance().beaconScanChanged(scanning);
                    break;

                case GeomobyService.NEW_FENCE_LIST:
                    ArrayList<GeomobyFenceView> fences = intent.getExtras().getParcelableArrayList("fences");
                    GeoMobyManager.getInstance().fenceListChanged(fences);
                    break;

                case GeoMoby.GeomobyActionData:
                    GeomobyActionData geomobyActionData = (GeomobyActionData) intent.getSerializableExtra(GeoMoby.GeomobyActionDataContent);
                    if (geomobyActionData != null) {
                        String key = "id";
                        String value = geomobyActionData.getValue(key);
                        if (value != null) {
                            Intent openIntent = new Intent(context, DiscountActivity.class);
                            openIntent.putExtra(key, value);
                            com.geomoby.demoapp.logic.system.NotificationManager.sendNotification(context, openIntent, "", "Data Action Received!", R.mipmap.data);
                        }
                    }
                    break;

                case GeoMoby.GeomobyActionBasic:
                    GeomobyActionBasic geomobyActionBasic = (GeomobyActionBasic) intent.getSerializableExtra(GeoMoby.GeomobyActionBasicContent);
                    if (geomobyActionBasic != null) {
                        Intent openIntent = new Intent(context, MainActivity.class);
                        com.geomoby.demoapp.logic.system.NotificationManager.sendNotification(context, openIntent, geomobyActionBasic.getTitle(), geomobyActionBasic.getBody(), R.mipmap.message);
                    }
                    break;
            }
        }
    };


    public GeoService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        IntentFilter filter = new IntentFilter();
        filter.addAction("BroadcastInitLocation");
        filter.addAction("GEOMOBY_NEW_DISTANCE");
        filter.addAction("GEOMOBY_BEACON_SCAN");
        filter.addAction("GEOMOBY_NEW_FENCE_LIST");

        filter.addAction("GeomobyActionData");
        filter.addAction("GeomobyActionBasic");
        getApplicationContext().registerReceiver(mReceiver, filter);


        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Geomoby demo app")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mReceiver != null)
            unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Location service",
                    NotificationManager.IMPORTANCE_MIN
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
