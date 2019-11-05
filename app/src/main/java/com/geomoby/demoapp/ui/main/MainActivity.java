package com.geomoby.demoapp.ui.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;

import com.geomoby.classes.GeomobyFenceView;
import com.geomoby.classes.GeomobyGeometryItem;
import com.geomoby.demoapp.R;

import com.geomoby.demoapp.ui.settings.SettingsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.PermissionToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MvpAppCompatActivity implements MainView, NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    @InjectPresenter
    MainPresenter mMainPresenter;

    private final int mFillColor = Color.argb((int)(255 * 0.5f), (int)(255 * 0.0f), (int)(255 * 0.72f), (int)(255 * 0.85f));
    private final int mBorderColor = Color.argb((int)(255 * 1.0f), (int)(255 * 0.0f), (int)(255 * 0.72f), (int)(255 * 0.85f));
    private final int mBeaconFillColor = Color.argb((int)(255 * 0.1f), (int)(255 * 0.0f), (int)(255 * 0.62f), (int)(255 * 0.95f));
    private final int mBeaconBorderColor = Color.argb((int)(255 * 0.0f), (int)(255 * 1.0f), (int)(255 * 1.0f), (int)(255 * 1.0f));
    private final float mStrokeWidth = 6.0f;

    private GoogleMap mMap = null;
    private SupportMapFragment mMapFragment;
    private DrawerLayout mDrawer;
    private TextView mMainTextNearestMeters;
    private TextView mMainLocationText;
    private TextView mMainBeaconText;
    private View mMainProgress;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mainMapFragment);
        mDrawer = findViewById(R.id.mainDrawerLayout);

        NavigationView navigationView = findViewById(R.id.mainNavigationView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ImageView mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        });

        mMainTextNearestMeters = findViewById(R.id.mainNearestMetersText);
        mMainLocationText = findViewById(R.id.mainLocationText);
        mMainBeaconText = findViewById(R.id.mainBeaconText);
        mMainProgress = findViewById(R.id.mainProgress);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mMainPresenter.activityStarted(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.activityDestroyed();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showPermissionRationale(final PermissionToken token) {
        new AlertDialog.Builder(this).setTitle(R.string.permission_rationale_title)
                .setMessage(R.string.permission_rationale_message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.cancelPermissionRequest();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        token.continuePermissionRequest();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override public void onDismiss(DialogInterface dialog) {
                        token.cancelPermissionRequest();
                    }
                })
                .show();
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mMainPresenter.handlePermissionResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onCheckPermissionRationale(final String permission, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mMainPresenter.handlePermissionRationale(permission, requestCode, shouldShowRequestPermissionRationale(permission));
        }
    }

    @Override
    public void onShowRationaleDialog(final String permission, final int requestCode, String text) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMainPresenter.rationaleClosed(permission, requestCode);
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onRequestForPermissions(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }*/

    @Override
    public void onShowProgress(boolean show) {
        if (show) {
            mMainProgress.setVisibility(View.VISIBLE);
        } else {
            mMainProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStartMap() {
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onSetMapModeStandard() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void onSetMapModeHybrid() {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    @Override
    public void onSetMapModeSatellite() {
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    @Override
    public void onInitLocationChanged(double latitude, double longitude) {
        if (mMap != null) {
            LatLng latlong = new LatLng(latitude, longitude);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, 14.0f));
        }
    }

    @Override
    public void onLocationChanged(double latitude, double longitude) {
        String result = latitude + " " + longitude;
        mMainLocationText.setText(result);
    }

    @Override
    public void onDistanceChanged(String distance) {
        String result = distance + "M";
        mMainTextNearestMeters.setText(result);
    }

    @Override
    public void onBeaconScanChanged(boolean scanning) {
        if (scanning) {
            mMainBeaconText.setText("Beacons scanning");
        } else {
            mMainBeaconText.setText("No beacons scanning");
        }
    }

    @Override
    public void onFenceListChanged(ArrayList<GeomobyFenceView> fences) {
        mMap.clear();

        for (GeomobyFenceView fence : fences) {
            switch (fence.getType()) {
                case "polygon":
                    addPolygon(fence);
                    break;
                case "point":
                    addCircle(fence);
                    break;
                case "beacon":
                    addBeacon(fence);
                    break;
                case "line":
                    addLine(fence);
                    break;
            }
        }
    }

    private void addPolygon(GeomobyFenceView fence) {
        List<GeomobyGeometryItem> geometries = fence.getGeometries();

        for (GeomobyGeometryItem geomerty : geometries) {
            List<Location> points = geomerty.getPoints();

            PolygonOptions polygonOptions = new PolygonOptions();
            for (Location point : points) {
                polygonOptions.add(new LatLng(point.getLatitude(), point.getLongitude()));
            }
            polygonOptions.fillColor(mFillColor);
            polygonOptions.strokeColor(mBorderColor);
            polygonOptions.strokeWidth(mStrokeWidth);
            mMap.addPolygon(polygonOptions);
        }
    }

    private void addCircle(GeomobyFenceView fence) {
        Location point = fence.getGeometries().get(0).getPoints().get(0);

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(new LatLng(point.getLatitude(), point.getLongitude()));
        circleOptions.radius(fence.getRadius());
        circleOptions.fillColor(mFillColor);
        circleOptions.strokeColor(mBorderColor);
        circleOptions.strokeWidth(mStrokeWidth);
        mMap.addCircle(circleOptions);
    }

    private void addBeacon(GeomobyFenceView fence) {
        Location point = fence.getGeometries().get(0).getPoints().get(0);

        CircleOptions beaconOptions = new CircleOptions()
                .center(new LatLng(point.getLatitude(), point.getLongitude()))
                .radius(fence.getRadius())
                .fillColor(mBeaconFillColor)
                .strokeColor(mBeaconBorderColor)
                .strokeWidth(mStrokeWidth);
        mMap.addCircle(beaconOptions);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(point.getLatitude(), point.getLongitude()))
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.beacon));
        mMap.addMarker(markerOptions);
    }

    private void addLine(GeomobyFenceView fence) {
        List<Location> points = fence.getGeometries().get(0).getPoints();

        PolylineOptions lineOptions = new PolylineOptions();
        for (Location point : points) {
            lineOptions.add(new LatLng(point.getLatitude(), point.getLongitude()));
        }
        lineOptions.color(mBorderColor);
        lineOptions.width(mStrokeWidth);
        mMap.addPolyline(lineOptions);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            /*case R.id.nav_logs: {
                break;
            }*/
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        //}
        mMainPresenter.mapReady();
    }
}