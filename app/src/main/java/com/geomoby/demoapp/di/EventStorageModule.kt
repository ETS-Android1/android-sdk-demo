package com.geomoby.demoapp.di

import android.content.Context
import com.geomoby.demoapp.data.event_storage.EventStorageSP
import com.geomoby.demoapp.domain.repositories.EventLogger
import com.geomoby.demoapp.domain.repositories.EventStorage
import com.geomoby.demoapp.domain.usecases.event_storage.AddEvent
import com.geomoby.demoapp.domain.usecases.event_storage.ClearEventsList
import com.geomoby.demoapp.domain.usecases.event_storage.EventStorageUseCases
import com.geomoby.demoapp.domain.usecases.event_storage.GetEventsList
import com.geomoby.demoapp.domain.usecases.logger.EventLoggerUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EventStorageModule {

    @Provides
    @Singleton
    fun provideEventStorage(@ApplicationContext context: Context): EventStorage {
        return EventStorageSP(context)
    }

    @Provides
    @Singleton
    fun provideEventStorageUseCases(repository: EventStorage): EventStorageUseCases {
        return EventStorageUseCases(
            addEvent = AddEvent(repository),
            clearEventsList = ClearEventsList(repository),
            getEventsList = GetEventsList(repository)
        )
    }
}