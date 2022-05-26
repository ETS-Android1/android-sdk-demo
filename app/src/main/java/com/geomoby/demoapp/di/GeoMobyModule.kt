package com.geomoby.demoapp.di

import com.geomoby.demoapp.data.geomoby.GeoMobyManager
import com.geomoby.demoapp.domain.repositories.GeomobyManager
import com.geomoby.demoapp.domain.usecases.geomoby.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GeoMobyModule {

    @Provides
    @Singleton
    fun provideGeoMobyManager(): GeomobyManager {
        return GeoMobyManager()
    }

    @Provides
    @Singleton
    fun provideGeoMobyUseCases(geomobyManager: GeomobyManager):GeoMobyUseCases {
        return GeoMobyUseCases(
            setDelegate = SetDelegate(geomobyManager),
            beaconScanChanged = BeaconScanChanged(geomobyManager),
            start = Start(geomobyManager),
            fenceListChanged = FenceListChanged(geomobyManager),
            initLocationChanged = InitLocationChanged(geomobyManager),
            distanceChanged = DistanceChanged(geomobyManager),
            updateFence = UpdateFence(geomobyManager),
            updateFirebaseId = UpdateFirebaseId(geomobyManager)
        )
    }
}