package com.geomoby.demoapp.domain.usecases.logger

import com.geomoby.demoapp.domain.repositories.EventLogger

class AddEvent(private val logger: EventLogger) {

    operator fun invoke(message:String){
        logger.addEvent(message)
    }
}