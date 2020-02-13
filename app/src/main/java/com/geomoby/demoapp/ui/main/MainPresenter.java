package com.geomoby.demoapp.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.geomoby.classes.GeomobyFenceView;
import com.geomoby.demoapp.GeoMobyApplication;
import com.geomoby.demoapp.logic.geomoby.GeoMobyManager;
import com.geomoby.demoapp.logic.location.LocationManager;
import com.geomoby.demoapp.logic.settings.SettingsManager;

import java.util.ArrayList;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> implements GeoMobyManager.GeoMobyManagerCallback, LocationManager.LocationManagerCallback {
    private static final int LOCATION_PERMISSION_RESPONSE = 4001;

    public void activityStarted() {
        getViewState().onShowProgress(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION_RESPONSE);
        } else {
            getViewState().onStartMap();
        }
    }

    public void activityDestroyed() {
        GeoMobyManager.getInstance().updateFences();
    }

    private void checkPermission(final String permission, int requestCode) {
        
        if (ContextCompat.checkSelfPermission(GeoMobyApplication.getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            getViewState().onCheckPermissionRationale(permission, requestCode);
        } else {
            getViewState().onStartMap();
        }
    }

    public void handlePermissionRationale(final String permission, int requestCode, boolean result) {
        if (!result) {
            getViewState().onShowRationaleDialog(permission, requestCode, "You need to allow access to your location");
        } else {
            requestPermission(permission, requestCode);
        }
    }

    public void rationaleClosed(final String permission, int requestCode) {
        requestPermission(permission, requestCode);
    }

    private void requestPermission(final String permission, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getViewState().onRequestForPermissions(new String[]{permission, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACTIVITY_RECOGNITION}, requestCode);
        } else {
            getViewState().onRequestForPermissions(new String[]{permission}, requestCode);
        }
    }

    public void handlePermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_RESPONSE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getViewState().onStartMap();
                    GeoMobyManager.getInstance().updateFences();
                }
            }
        }
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
