package com.geomoby.demoapp.logic.geomoby;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.geomoby.classes.GeomobyFenceView;
import com.geomoby.managers.GeomobyDataManager;
import com.geomoby.managers.GeomobyGPSManager;
import com.geomoby.services.GeomobyService;

import java.util.ArrayList;

public class GeoMobyStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) return;

        switch(action) {
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
        }
    }
}
