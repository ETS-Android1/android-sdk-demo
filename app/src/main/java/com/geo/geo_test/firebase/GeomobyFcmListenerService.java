package com.geo.geo_test.firebase;

import android.util.Log;

import com.geomoby.GeoMoby;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class GeomobyFcmListenerService extends FirebaseMessagingService {

    private static final String TAG = "GeomobyFbMsgSrv";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getFrom().equals("/topics/GeomobySync")) {

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());

                String messageType = remoteMessage.getData().get("MessageType");
                if (messageType.equals("GeomobySyncRequest")) {
                    Log.d(TAG, "GeomobySyncRequest accepted");

                    // Update fence list
                    GeoMoby.updateFences();
                }
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
        }
    }
}