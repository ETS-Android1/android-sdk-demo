package com.geomoby.demoapp.di

import android.content.Context
import com.geomoby.demoapp.data.map_mode.SharedPreferencesMapMode
import com.geomoby.demoapp.domain.repositories.MapMode
import com.geomoby.demoapp.domain.usecases.map_mode.GetMapMode
import com.geomoby.demoapp.domain.usecases.map_mode.MapModeUseCases
import com.geomoby.demoapp.domain.usecases.map_mode.SetMapMode
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MapModeModule {

    @Provides
    @Singleton
    fun provideMapMode(@ApplicationContext context: Context):MapMode{
        return SharedPreferencesMapMode(context)
    }

    @Provides
    @Singleton
    fun provideMapModeUseCases(repository: MapMode):MapModeUseCases{
        return MapModeUseCases(
            getMapMode = GetMapMode(repository),
            setMapMode = SetMapMode(repository)
        )
    }
}