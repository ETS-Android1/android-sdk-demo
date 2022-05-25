package com.geomoby.demoapp.domain.usecases.event_storage

import com.geomoby.demoapp.domain.repositories.EventStorage

class AddEvent(private val logger: EventStorage) {

    operator fun invoke(event: EventStorage.Event){
        logger.addEvent(event)
    }
}