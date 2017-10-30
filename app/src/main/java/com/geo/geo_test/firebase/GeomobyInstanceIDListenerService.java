package com.geo.geo_test.firebase;

import android.content.Intent;

import com.geomoby.GeoMoby;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class GeomobyInstanceIDListenerService extends FirebaseInstanceIdService {

    public static final String NEW_INSTANCE_ID = "BroadcastInstanceId";


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        GeoMoby.updateInstanceId(token);
    }
}
