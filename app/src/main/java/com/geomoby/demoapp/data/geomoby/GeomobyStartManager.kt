package com.geomoby.demoapp.data.geomoby

import android.content.Context
import com.geomoby.GeoMoby
import com.geomoby.callbacks.GeomobyServiceCallback
import com.geomoby.classes.GeomobyError
import com.geomoby.demoapp.ui.firebase.FirebaseManager
import java.util.HashMap

class GeomobyStartManager : GeomobyServiceCallback {
    fun start(context: Context){
        // Build geomoby. build() method returns Geomoby object
        GeoMoby.Builder(context, "OCSQSV3P", this)
            .setDevMode(true)
            .setUUID("f7826da6-4fa2-4e98-8024-bc5b71e0893e")
            .setOfflineMode(true)
            .setSilenceWindow(23, 0)
            .build()
        val tags: MutableMap<String?, String?> = HashMap()
        tags["gender"] = "male"
        tags["age"] = "27"
        tags["membership"] = "gold"
        GeoMoby.setTags(tags)
    }

    override fun onError(error: GeomobyError?) {
    }

    override fun onStarted() {
        FirebaseManager.initFirebase()
    }

    override fun onStopped() {
    }
}
