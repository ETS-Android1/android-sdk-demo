package com.geomoby.demoapp.logic.geomoby;

import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.geomoby.classes.GeomobyError;
import com.geomoby.GeoMoby;
import com.geomoby.callbacks.GeomobyServiceCallback;
import com.geomoby.classes.GeomobyFenceView;
import com.geomoby.demoapp.GeoMobyApplication;
import com.geomoby.demoapp.R;
import com.geomoby.demoapp.logic.firebase.FirebaseManager;
import com.geomoby.managers.GeomobyDataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GeoMobyManager implements GeomobyServiceCallback {
    private final String TAG = GeoMobyManager.class.getSimpleName();

    private static GeoMobyManager mInstance = null;

    private GeoMobyManagerCallback mDelegate = null;
    private boolean mStarted = false;

    public static GeoMobyManager getInstance() {
        if (mInstance == null) {
            mInstance = new GeoMobyManager();
        }
        return mInstance;
    }


    private GeoMobyManager() {
        // Build geomoby. build() method returns Geomoby object
        new GeoMoby.Builder(GeoMobyApplication.getContext(), "OCSQSV3P", this)
                .setDevMode(true)
                .setUUID("f7826da6-4fa2-4e98-8024-bc5b71e0893e")
                .setOfflineMode(true)
                .setSilenceWindow(23,0)
                .build();

        Map<String, String> tags = new HashMap<>();
        tags.put("gender", "male");
        tags.put("age", "27");
        tags.put("membership", "gold");
        GeoMoby.Companion.setTags(tags);
    }

    public void setDelegate(GeoMobyManagerCallback delegate) {
        mDelegate = delegate;
        // Initial states
        if (mDelegate != null) {

            Location initLocation = GeomobyDataManager.Companion.getInstance().getInitLocation();
            if (initLocation != null) {
                mDelegate.onInitLocationChanged(initLocation);
            }

            String distance = GeomobyDataManager.Companion.getInstance().getMinDistance();
            boolean inside =  GeomobyDataManager.Companion.getInstance().getInside();
            if (distance != null) {
                mDelegate.onDistanceChanged(distance, inside);
            }

            boolean scanning =  GeomobyDataManager.Companion.getInstance().getScanning();
            mDelegate.onBeaconScanChanged(scanning);

            ArrayList<GeomobyFenceView> fences = GeomobyDataManager.Companion.getInstance().getFenceViews();
            if (fences != null) {
                mDelegate.onFenceListChanged(fences);
            }
        }
    }

    public void start() {
        if (!mStarted) {
            GeoMoby.Companion.start();
            mStarted = true;
        }
    }

    public void updateFences() {
        GeoMoby.Companion.updateFences();
    }

    public void updateFirebaseId(String firebaseId) {
        GeoMoby.Companion.updateInstanceId(firebaseId);
    }

    public void initLocationChanged(Location location) {
        if (mDelegate != null) {
            mDelegate.onInitLocationChanged(location);
        }
    }

    public void distanceChanged(String distance, boolean inside) {
        if (mDelegate != null) {
            mDelegate.onDistanceChanged(distance, inside);
        }
    }

    public void beaconScanChanged(boolean scanning) {
        if (mDelegate != null) {
            mDelegate.onBeaconScanChanged(scanning);
        }
    }

    public void fenceListChanged(ArrayList<GeomobyFenceView> fences) {
        if (mDelegate != null) {
            mDelegate.onFenceListChanged(fences);
        }
    }

    @Override
    public void onStarted() {
        Log.d(TAG, "Service started!");
        FirebaseManager.initFirebase();
    }

    @Override
    public void onStopped() {
        Log.d(TAG, "Service stopped!");
    }

    @Override
    public void onError(GeomobyError geomobyError) {
        Log.d(TAG, "Error - " + geomobyError.getMessage() + "!");
    }

    public interface GeoMobyManagerCallback {
        void onInitLocationChanged(Location location);
        void onDistanceChanged(String distance, boolean inside);
        void onBeaconScanChanged(boolean scanning);
        void onFenceListChanged(ArrayList<GeomobyFenceView> fences);
    }
}
