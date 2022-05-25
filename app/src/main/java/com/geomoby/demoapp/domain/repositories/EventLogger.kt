package com.geomoby.demoapp.domain.repositories

import android.app.Activity

interface EventLogger{
    fun addEvent(message: String)
    fun getAllLogData():String?
    fun clearAllLogs()
    fun sendLog(activity: Activity)
    fun clearAllOldLogs()
}