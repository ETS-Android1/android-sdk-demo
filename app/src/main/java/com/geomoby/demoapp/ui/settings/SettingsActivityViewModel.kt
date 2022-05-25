package com.geomoby.demoapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geomoby.demoapp.domain.repositories.MapMode
import com.geomoby.demoapp.domain.usecases.map_mode.MapModeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsActivityViewModel @Inject constructor(
    private val mapModeUseCases: MapModeUseCases
):ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: SettingsEvent) {
        when(event){
            is SettingsEvent.SetMapMode ->  {
                mapModeUseCases.setMapMode(event.mode)
                viewModelScope.launch {
                    when(event.mode){
                        MapMode.MAP_MODE_STANDARD -> _eventFlow.emit(UiEvent.MapModeStandard)
                        MapMode.MAP_MODE_HYBRID -> _eventFlow.emit(UiEvent.MapModeHybrid)
                        MapMode.MAP_MODE_SATELLITE -> _eventFlow.emit(UiEvent.MapModeSatellite)
                    }

                }
            }
            is SettingsEvent.GetMapMode -> {
                viewModelScope.launch {
                    when(mapModeUseCases.getMapMode()){
                        MapMode.MAP_MODE_STANDARD -> _eventFlow.emit(UiEvent.MapModeStandard)
                        MapMode.MAP_MODE_HYBRID -> _eventFlow.emit(UiEvent.MapModeHybrid)
                        MapMode.MAP_MODE_SATELLITE -> _eventFlow.emit(UiEvent.MapModeSatellite)
                    }
                }
            }
        }
    }

    sealed class UiEvent{
        object MapModeStandard:UiEvent()
        object MapModeHybrid:UiEvent()
        object MapModeSatellite:UiEvent()
    }
}