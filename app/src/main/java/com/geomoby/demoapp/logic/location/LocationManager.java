package com.geomoby.demoapp.logic.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.geomoby.demoapp.GeoMobyApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


public class LocationManager extends LocationCallback implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final long UPDATE_INTERVAL_MILLISECONDS = 10 * 1000;
    private static final long FASTEST_INTERVAL_MILLISECONDS = 2 * 1000;

    private static LocationManager mInstance = null;

    private LocationManagerCallback mDelegate = null;
    private GoogleApiClient mGoogleApiClient;


    public static LocationManager getInstance() {
        if (mInstance == null) {
            mInstance = new LocationManager();
        }
        return mInstance;
    }

    private LocationManager() {
        mGoogleApiClient = new GoogleApiClient.Builder(GeoMobyApplication.getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    public void setDelegate(LocationManagerCallback delegate) {
        mDelegate = delegate;
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_NO_POWER);
        locationRequest.setInterval(UPDATE_INTERVAL_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_INTERVAL_MILLISECONDS);
        return locationRequest;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(GeoMobyApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(GeoMobyApplication.getContext()).requestLocationUpdates(getLocationRequest(), this, Looper.myLooper());
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        if (mDelegate != null) {
            mDelegate.onLocationChanged(locationResult.getLastLocation());
        }
    }

    public interface LocationManagerCallback {
        void onLocationChanged(Location location);
    }
}