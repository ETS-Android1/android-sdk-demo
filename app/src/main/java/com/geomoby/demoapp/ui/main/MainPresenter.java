package com.geomoby.demoapp.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.geomoby.classes.GeomobyFenceView;
import com.geomoby.demoapp.GeoMobyApplication;
import com.geomoby.demoapp.logic.geomoby.GeoMobyManager;
import com.geomoby.demoapp.logic.location.LocationManager;
import com.geomoby.demoapp.logic.settings.SettingsManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> implements GeoMobyManager.GeoMobyManagerCallback, LocationManager.LocationManagerCallback {
    private static final int LOCATION_PERMISSION_RESPONSE = 4001;

    public void activityStarted(Activity activity) {
        getViewState().onShowProgress(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION_RESPONSE);

            Dexter.withActivity(activity)
                    .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            Log.e("Dexter", "Permissions Granted!");
                            getViewState().onStartMap();
                            GeoMobyManager.getInstance().updateFences();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            Log.e("Dexter", "Permissions should be shown!");
                            getViewState().showPermissionRationale(token);
                            //token.continuePermissionRequest();
                        }
            }).withErrorListener(new PermissionRequestErrorListener() {
                @Override public void onError(DexterError error) {
                    Log.e("Dexter", "There was an error: " + error.toString());
                }
            }).check();

        } else {
            getViewState().onStartMap();
            GeoMobyManager.getInstance().updateFences();
        }
    }

    public void activityDestroyed() {
        GeoMobyManager.getInstance().updateFences();
    }
    
    public void mapReady() {

        int mapMode = SettingsManager.getInstance().getMapMode();
        switch (mapMode) {
            case SettingsManager.MAP_MODE_STANDARD:
                getViewState().onSetMapModeStandard();
                break;
            case SettingsManager.MAP_MODE_HYBRID:
                getViewState().onSetMapModeHybrid();
                break;
            case SettingsManager.MAP_MODE_SATELLITE:
                getViewState().onSetMapModeSatellite();
                break;
        }

        startGeoMobyManager();
        startLocationManager();
    }

    private void startGeoMobyManager() {
        //GeoMobyManager.getInstance().start();
        GeoMobyManager.getInstance().setDelegate(this);
    }

    private void startLocationManager() {
        LocationManager.getInstance();
        LocationManager.getInstance().setDelegate(this);
    }

    @Override
    public void onInitLocationChanged(Location location) {
        getViewState().onInitLocationChanged(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onDistanceChanged(String distance, boolean inside) {
        String result = inside ? "-" + distance : distance;
        getViewState().onDistanceChanged(result);
    }

    @Override
    public void onBeaconScanChanged(boolean scanning) {
        getViewState().onBeaconScanChanged(scanning);
    }

    @Override
    public void onFenceListChanged(ArrayList<GeomobyFenceView> fences) {
        getViewState().onShowProgress(false);
        getViewState().onFenceListChanged(fences);
    }

    @Override
    public void onLocationChanged(Location location) {
        getViewState().onLocationChanged(location.getLatitude(), location.getLongitude());
    }
}
