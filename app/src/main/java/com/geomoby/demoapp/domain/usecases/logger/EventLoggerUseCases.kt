package com.geomoby.demoapp.domain.usecases.logger

data class EventLoggerUseCases(
    val addEvent: AddEvent,
    val getAllLogData: GetAllLogData,
    val clearAllLogs: ClearAllLogs,
    val sendLog: SendLog
)