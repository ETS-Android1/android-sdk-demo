package com.geomoby.demoapp.logic.firebase;

import android.util.Log;

import com.geomoby.demoapp.logic.geomoby.GeoMobyManager;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseManager extends FirebaseMessagingService {
    private static final String TAG = FirebaseManager.class.getSimpleName();

    public static void initFirebase() {
        // Subsribe to topic
        FirebaseMessaging.getInstance().subscribeToTopic("GeomobySync");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if ((remoteMessage.getFrom() != null) && (remoteMessage.getFrom().equals("/topics/GeomobySync"))) {

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());

                String messageType = remoteMessage.getData().get("MessageType");
                if (messageType.equals("GeomobySyncRequest")) {
                    Log.d(TAG, "GeomobySyncRequest accepted");

                    // Update fence list
                    GeoMobyManager.getInstance().updateFences();
                }
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
        }
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        Log.d(TAG, "Token:  " + token);

        // Update firebase id
        GeoMobyManager.getInstance().updateFirebaseId(token);
    }
}