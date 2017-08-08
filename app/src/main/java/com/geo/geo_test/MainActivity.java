package com.geo.geo_test;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import com.geomoby.GeoMoby;
import com.geomoby.callbacks.GeomobyServiceCallback;
import com.geomoby.classes.GeomobyError;
import com.geomoby.managers.GeomobyDataManager;
import com.geomoby.managers.GeomobyGPSManager;
import com.geomoby.services.GeomobyService;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    GeoMoby mGeoMoby;
    TextView initLoc;
    TextView curLoc;
    TextView repInt;
    TextView minDist;
    TextView reportStatus;
    TextView scanStatus;
    TextView avgSpeed;
    TextView curSpeed;
    TextView gpsAccuracy;
    TextView accuracy;
    Timer timer;
    int timeFromLastReport = 0;
    boolean firstSend = false;
    TimerTask timerTask  = new TimerTask() {
        @Override
        public void run() {
            timeFromLastReport++;
            if (firstSend){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reportStatus.setText("Report sent (" + timeFromLastReport + " s  from last report)");
                    }
                });
            }
        }
    };
    private static final int MY_PERMISSION_RESPONSE = 42;




    // Reciever of messages
    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // Get new init location
            if(action.equals(GeomobyGPSManager.NEW_INIT_LOCATION)){
                Location location = GeomobyDataManager.getInstance().getInitLocation();
                initLoc.setText("" + location.getLatitude() + ", " + location.getLongitude());
            }

            // Get new location
            else if(action.equals(GeomobyGPSManager.NEW_CURRENT_LOCATION)){
                Location initLocation = GeomobyDataManager.getInstance().getInitLocation();
                Location currentLocation = GeomobyDataManager.getInstance().getCurrentLocation();
                float distance = currentLocation.distanceTo(initLocation);
                curLoc.setText("" + currentLocation.getLatitude() + ", " + currentLocation.getLongitude() + "; Distance to init = " + distance + "m");
            }

            // Distance
            else if(action.equals(GeomobyService.NEW_DISTANCE)){

                String distance = intent.getExtras().getString("distance");
                boolean inside = intent.getExtras().getBoolean("inside");
                if (!inside)
                    minDist.setText("Outside: " + distance + " m to nearest fence");
                else
                    minDist.setText("Inside: " + distance + " m to nearest border");
            }

            // Interval
            else if(action.equals(GeomobyService.NEW_INTERVAL)){
                int interval = intent.getExtras().getInt("interval");
                float avgspeed = intent.getExtras().getFloat("avgspeed");
                float speed = intent.getExtras().getFloat("speed");
                float gpsaccur = intent.getExtras().getFloat("gpsaccuracy");
                int accur = intent.getExtras().getInt("accuracy");
                repInt.setText("" + interval + " s");
                avgSpeed.setText("" + avgspeed + " m/s");
                curSpeed.setText("" + speed + " m/s");
                gpsAccuracy.setText("" + gpsaccur + " m");
                if (accur == 1)
                    accuracy.setText("BALANCED_POWER");
                else if (accur == 2)
                    accuracy.setText("HIGH_ACCURACY");
            }

            // Report sent
            else if(action.equals(GeomobyService.REPORT_SENT)){
                if (!firstSend)
                    firstSend = true;
                timeFromLastReport = 0;
            }

            // Beacon scan
            else if(action.equals(GeomobyService.BEACON_SCAN)){
                boolean scanning = intent.getExtras().getBoolean("scanning");
                if (scanning)
                    scanStatus.setText("true");
                else
                    scanStatus.setText("false");
            }
        }
    };




    // Service actions callback
    GeomobyServiceCallback mServiceCallback = new GeomobyServiceCallback(){

        @Override
        public void onStarted() {
            Log.d("Geomoby_Test", "Service started!");
        }

        @Override
        public void onStopped() {
            Log.d("Geomoby_Test", "Service stopped!");
        }

        @Override
        public void onError(GeomobyError error) {
            Log.d("Geomoby_Test", "Error - " + error.getMessage() + "!");
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLoc = (TextView)findViewById(R.id.init_geo_loc);
        curLoc = (TextView)findViewById(R.id.geo_loc);
        repInt = (TextView)findViewById(R.id.report_interval);
        minDist = (TextView)findViewById(R.id.distance);
        reportStatus = (TextView)findViewById(R.id.report_status);
        scanStatus = (TextView)findViewById(R.id.scan_status);
        avgSpeed = (TextView)findViewById(R.id.avg_speed);
        curSpeed = (TextView)findViewById(R.id.speed);
        gpsAccuracy = (TextView)findViewById(R.id.gps_accuracy);
        accuracy = (TextView)findViewById(R.id.accuracy);
        timer = new Timer();
        timer.schedule(timerTask, 1000, 1000);

        IntentFilter filter = new IntentFilter();
        filter.addAction(GeomobyGPSManager.NEW_INIT_LOCATION);
        filter.addAction(GeomobyGPSManager.NEW_CURRENT_LOCATION);
        filter.addAction(GeomobyService.NEW_DISTANCE);
        filter.addAction(GeomobyService.NEW_INTERVAL);
        filter.addAction(GeomobyService.REPORT_SENT);
        filter.addAction(GeomobyService.BEACON_SCAN);
        registerReceiver(intentReceiver, filter);


        //Request permission if Android >= 6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity", "Location access not granted!");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Permission needed")
                        .setMessage("Plese allow app to use Location")
                        .setCancelable(false)
                        .setNegativeButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                            MainActivity.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_RESPONSE);
                                        }
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
            else{
                startGeomoby();
            }
        }
        else{
            startGeomoby();
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        unregisterReceiver(intentReceiver);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSION_RESPONSE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity", "Location access granted!");
                    startGeomoby();
                } else {
                    Log.d("MainActivity", "Location access denied!");
                }
                return;
            }
        }
    }




    void startGeomoby(){
        Map<String, String> tags = new HashMap<>();
        tags.put("gender", "female");
        tags.put("age", "25");
        tags.put("membership", "gold");

        mGeoMoby = new GeoMoby.Builder(getApplicationContext(), "XXXXXXXX", mServiceCallback)
                .setDevMode(true)
                .setUUID("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx")
                .setSilenceWindow(23,5)
                .setTags(tags)
                .build();

        mGeoMoby.start();


        Location initLocation = GeomobyDataManager.getInstance().getInitLocation();
        Location currentLocation = GeomobyDataManager.getInstance().getCurrentLocation();

        if (initLocation != null) {
            initLoc.setText("" + initLocation.getLatitude() + ", " + initLocation.getLongitude());
        }

        if (initLocation != null && currentLocation != null) {
            float distance = currentLocation.distanceTo(initLocation);
            curLoc.setText("" + currentLocation.getLatitude() + ", " + currentLocation.getLongitude() + "; Distance to init = " + distance + "m");
        }
    }
}
