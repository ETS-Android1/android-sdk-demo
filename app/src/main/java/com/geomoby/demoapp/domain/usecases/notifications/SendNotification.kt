package com.geomoby.demoapp.domain.usecases.notifications

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import com.geomoby.demoapp.domain.repositories.Notifications

class SendNotification(val notifications: Notifications) {

    operator fun invoke(
        context: Context,
        intent: Intent?,
        title: String?,
        body: String?,
        @DrawableRes icon: Int
    ){
        notifications.sendNotification(context,intent, title, body, icon)
    }
}