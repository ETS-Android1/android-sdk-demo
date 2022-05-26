package com.geomoby.demoapp.domain.repositories

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes

interface Notifications {

    fun sendNotification(
        context: Context,
        intent: Intent?,
        title: String?,
        body: String?,
        @DrawableRes icon: Int
    )
}