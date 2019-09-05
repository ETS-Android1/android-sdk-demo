package com.geomoby.demoapp.logic.geomoby;

import android.location.Location;
import android.util.Log;

import com.geomoby.GeoMoby;
import com.geomoby.callbacks.GeomobyServiceCallback;
import com.geomoby.classes.GeomobyError;
import com.geomoby.classes.GeomobyFenceView;
import com.geomoby.demoapp.GeoMobyApplication;
import com.geomoby.demoapp.R;
import com.geomoby.demoapp.logic.firebase.FirebaseManager;
import com.geomoby.managers.GeomobyDataManager;

import java.util.ArrayList;

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
        new GeoMoby.Builder(GeoMobyApplication.getContext(), "ZR4OWDRF", this)
                .setDevMode(true)
                .setUUID("30cab38c-6921-43f4-b005-24af1e070ff2")
                .setSilenceWindow(23,5)
                .setForeground(GeoMobyApplication.getContext().getResources().getString(R.string.app_name),
                        "This notification shows that application is working", R.mipmap.ic_launcher)
                .forceForeground(true)
                .build();
    }

    public void setDelegate(GeoMobyManagerCallback delegate) {
        mDelegate = delegate;

        // Initial states
        if (mDelegate != null) {

            Location initLocation = GeomobyDataManager.getInstance().getInitLocation();
            if (initLocation != null) {
                mDelegate.onInitLocationChanged(initLocation);
            }

            String distance = GeomobyDataManager.getInstance().getMinDistance();
            boolean inside =  GeomobyDataManager.getInstance().getInside();
            if (distance != null) {
                mDelegate.onDistanceChanged(distance, inside);
            }

            boolean scanning =  GeomobyDataManager.getInstance().getScanning();
            mDelegate.onBeaconScanChanged(scanning);

            ArrayList<GeomobyFenceView> fences = GeomobyDataManager.getInstance().getFenceViews();
            if (fences != null) {
                mDelegate.onFenceListChanged(fences);
            }
        }
    }

    public void start() {
        if (!mStarted) {
            GeoMoby.start();
            mStarted = true;
        }
    }

    public void updateFences() {
        GeoMoby.updateFences();
    }

    public void updateFirebaseId(String firebaseId) {
        GeoMoby.updateInstanceId(firebaseId);
    }

    void initLocationChanged(Location location) {
        if (mDelegate != null) {
            mDelegate.onInitLocationChanged(location);
        }
    }

    void distanceChanged(String distance, boolean inside) {
        if (mDelegate != null) {
            mDelegate.onDistanceChanged(distance, inside);
        }
    }

    void beaconScanChanged(boolean scanning) {
        if (mDelegate != null) {
            mDelegate.onBeaconScanChanged(scanning);
        }
    }

    void fenceListChanged(ArrayList<GeomobyFenceView> fences) {
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
