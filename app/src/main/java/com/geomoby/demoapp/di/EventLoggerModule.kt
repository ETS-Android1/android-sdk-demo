package com.geomoby.demoapp.di

import android.content.Context
import com.geomoby.demoapp.data.event_logger.EventLoggerFile
import com.geomoby.demoapp.domain.repositories.EventLogger
import com.geomoby.demoapp.domain.usecases.logger.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EventLoggerModule {

    @Provides
    @Singleton
    fun provideEventLogger(@ApplicationContext context: Context):EventLogger{
        return EventLoggerFile(context)
    }

    @Provides
    @Singleton
    fun provideEventLoggerUseCases(repository: EventLogger): EventLoggerUseCases {
        return EventLoggerUseCases(
            addEvent = AddEvent(repository),
            getAllLogData = GetAllLogData(repository),
            clearAllLogs = ClearAllLogs(repository),
            sendLog = SendLog(repository)
        )
    }
}