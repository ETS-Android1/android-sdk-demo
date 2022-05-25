package com.geomoby.demoapp.domain.usecases.event_storage

import com.geomoby.demoapp.domain.repositories.EventStorage

class ClearEventsList(private val logger: EventStorage) {

    operator fun invoke(){
        logger.clearEventsList()
    }
}