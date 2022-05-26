package com.geomoby.demoapp.di

import android.content.Context
import com.geomoby.demoapp.data.location.LocationManagerGoogle
import com.geomoby.demoapp.domain.repositories.LocationManager
import com.geomoby.demoapp.domain.usecases.location_manager.LocationManagerUseCases
import com.geomoby.demoapp.domain.usecases.location_manager.SetDelegate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocationManagerModule {

    @Provides
    @Singleton
    fun provideLocationManager(@ApplicationContext context: Context):LocationManager{
        return LocationManagerGoogle(context)
    }

    @Provides
    @Singleton
    fun providesLocationManagerUseCases(locationManager: LocationManager):LocationManagerUseCases{
        return LocationManagerUseCases(
            setDelegate = SetDelegate(locationManager)
        )
    }
}