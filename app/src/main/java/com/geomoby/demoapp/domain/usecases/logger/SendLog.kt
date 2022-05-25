package com.geomoby.demoapp.domain.usecases.logger

import android.app.Activity
import com.geomoby.demoapp.domain.repositories.EventLogger

class SendLog(private val logger: EventLogger) {

    operator fun invoke(activity: Activity){
        logger.sendLog(activity)
    }
}