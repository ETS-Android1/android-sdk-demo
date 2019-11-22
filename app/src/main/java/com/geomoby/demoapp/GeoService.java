package com.geomoby.demoapp;

import android.content.Intent;
import android.location.Location;

import com.geomoby.GeomobyUserService;
import com.geomoby.classes.GeomobyActionBasic;
import com.geomoby.classes.GeomobyActionData;
import com.geomoby.classes.GeomobyFenceView;
import com.geomoby.demoapp.logic.geomoby.GeoMobyManager;
import com.geomoby.demoapp.logic.system.NotificationManager;
import com.geomoby.demoapp.ui.discount.DiscountActivity;
import com.geomoby.demoapp.ui.main.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class GeoService extends GeomobyUserService {

    @Override
    public void beaconScan(boolean scanning) {
        GeoMobyManager.getInstance().beaconScanChanged(scanning);
    }

    @Override
    public void geomobyActionBasic(@Nullable GeomobyActionBasic geomobyActionBasic) {
        if (geomobyActionBasic != null) {
            Intent openIntent = new Intent(this, MainActivity.class);
            NotificationManager.sendNotification(this, openIntent, geomobyActionBasic.getTitle(), geomobyActionBasic.getBody(), R.mipmap.message);
        }
    }

    @Override
    public void geomobyActionData(@Nullable GeomobyActionData geomobyActionData) {
        if (geomobyActionData != null) {
            String key = "id";
            String value = geomobyActionData.getValue(key);
            if (value != null) {
                Intent openIntent = new Intent(this, DiscountActivity.class);
                openIntent.putExtra(key, value);
                NotificationManager.sendNotification(this, openIntent, "", "Data Action Received!", R.mipmap.data);
            }
        }
    }

    @Override
    public void newDistanse(@Nullable String distance, boolean inside) {
        GeoMobyManager.getInstance().distanceChanged(distance, inside);
    }

    @Override
    public void newFenceList(@Nullable ArrayList<GeomobyFenceView> fences) {
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
    public int getNotificationSmallIconId() {
        return R.mipmap.ic_launcher;
    }

    @NotNull
    @Override
    public String getNotificationTitle() {
        return "Geomoby demo app";
    }

}
