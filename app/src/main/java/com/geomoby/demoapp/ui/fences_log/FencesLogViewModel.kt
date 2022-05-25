package com.geomoby.demoapp.ui.fences_log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geomoby.demoapp.domain.repositories.EventLogger
import com.geomoby.demoapp.ui.settings.SettingsActivityViewModel
import com.geomoby.demoapp.ui.settings.SettingsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FencesLogViewModel @Inject constructor(
    val logger: EventLogger
): ViewModel(){

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: FenceLogEvent) {
        when(event){
            is FenceLogEvent.GetFenceLog -> {
                viewModelScope.launch {
                    _eventFlow.emit( UiEvent.LogsUploaded(logger.getAllLogData()))
                }
            }
            is FenceLogEvent.ClearLog -> {
                logger.clearAllLogs()
            }
            is FenceLogEvent.SendLog -> {
                logger.sendLog(event.activity)
            }
        }
    }

    sealed class UiEvent{
        data class LogsUploaded(val logStr:String?):UiEvent()
    }
}