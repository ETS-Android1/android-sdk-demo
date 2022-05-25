package com.geomoby.demoapp.ui.fence_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geomoby.demoapp.domain.repositories.EventStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FenceHistoryViewModel @Inject constructor(
    val eventStorage: EventStorage
):ViewModel(){

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: FenceHistoryEvent) {
        when(event){
            is FenceHistoryEvent.GetEventsList -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.EventListUploaded(
                        eventStorage.getEventsList()
                    ))
                }
            }
            is FenceHistoryEvent.ClearEventsList -> {
                eventStorage.clearEventsList()
            }
        }
    }

    sealed class UiEvent{
        data class EventListUploaded(val events:List<EventStorage.Event>): UiEvent()
    }
}