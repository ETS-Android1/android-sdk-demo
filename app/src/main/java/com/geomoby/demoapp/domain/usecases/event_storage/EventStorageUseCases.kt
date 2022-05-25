package com.geomoby.demoapp.domain.usecases.event_storage

data class EventStorageUseCases(
    val addEvent: AddEvent,
    val clearEventsList: ClearEventsList,
    val getEventsList: GetEventsList
)
