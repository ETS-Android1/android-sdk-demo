package com.geomoby.demoapp.ui.fence_history


sealed class FenceHistoryEvent {
    object GetEventsList: FenceHistoryEvent()
    object ClearEventsList: FenceHistoryEvent()
}