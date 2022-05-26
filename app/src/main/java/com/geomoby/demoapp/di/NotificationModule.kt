package com.geomoby.demoapp.di

import com.geomoby.demoapp.data.system.NotificationManager
import com.geomoby.demoapp.domain.repositories.Notifications
import com.geomoby.demoapp.domain.usecases.notifications.NotificationsUseCase
import com.geomoby.demoapp.domain.usecases.notifications.SendNotification
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NotificationModule {

    @Provides
    @Singleton
    fun provideNotificationRepository(): Notifications {
        return NotificationManager
    }

    @Provides
    @Singleton
    fun provideNotificationsUseCases(notifications: Notifications):NotificationsUseCase{
        return NotificationsUseCase(
            SendNotification(notifications)
        )
    }
}