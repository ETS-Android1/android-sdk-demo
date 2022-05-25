package com.geomoby.demoapp.ui.fences_log

import android.app.Activity

sealed class FenceLogEvent {
    object GetFenceLog:FenceLogEvent()
    object ClearLog:FenceLogEvent()
    data class SendLog(val activity: Activity):FenceLogEvent()
}