package com.geomoby.demoapp.domain.usecases.logger

import com.geomoby.demoapp.domain.repositories.EventLogger

class ClearAllLogs(private val logger: EventLogger) {

    operator fun invoke(){
        logger.clearAllLogs()
    }
}