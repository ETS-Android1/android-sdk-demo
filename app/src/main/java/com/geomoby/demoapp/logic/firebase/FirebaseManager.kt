package com.geomoby.demoapp.logic.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.geomoby.demoapp.logic.firebase.FirebaseManager
import com.geomoby.demoapp.logic.geomoby.GeoMobyManager
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseManager @Inject constructor(var geomobyManager: GeoMobyManager)  : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.from)
        if (remoteMessage.from != null && remoteMessage.from == "/topics/GeomobySync") {

            // Check if message contains a data payload.
            if (remoteMessage.data.isNotEmpty()) {
                Log.d(TAG, "Message data payload: " + remoteMessage.data)
                val messageType = remoteMessage.data["MessageType"]
                if (messageType == "GeomobySyncRequest") {
                    Log.d(TAG, "GeomobySyncRequest accepted")
                    // Update fence list
                    geomobyManager.updateFences()
                }
            }

            // Check if message contains a notification payload.
            remoteMessage.notification?.let {
                Log.d(TAG, "Message Notification Body: ${it.body}")
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Token:  $token")

        // Update firebase id
        geomobyManager.updateFirebaseId(token)
    }

    companion object {
        private val TAG = FirebaseManager::class.java.simpleName
        @JvmStatic
        fun initFirebase() {
            // Subscribe to topic
            FirebaseMessaging.getInstance().subscribeToTopic("GeomobySync")
        }
    }
}