package com.geomoby.demoapp.data.event_logger

import android.app.Activity
import android.content.Context
import android.util.Log
import com.geomoby.demoapp.BuildConfig
import com.geomoby.demoapp.domain.repositories.EventLogger
import java.text.SimpleDateFormat
import java.util.*

class EventLoggerFile(val context:Context): EventLogger {

    override fun addEvent(message: String) {
        putToLog(TYPE_EVENT, message)
    }

    override fun getAllLogData() = LogProvider.ReadFile.readListFromLogFile(context)?.joinToString("\n")
    override fun clearAllLogs(){
        LogProvider.ClearFile.clear(context)
    }

    private fun putToLog(type:String, message:String){
        val logStr = String.format("%s-%d %s:%s", SimpleDateFormat(LogProvider.TEMPLATE).format(Date()),
            BuildConfig.VERSION_CODE,type,message)
        Log.d("Log", "try to write - $logStr")
        LogProvider.WriteFile.writeToLogFile(context, logStr)
    }

    override fun sendLog(activity:Activity){
        LogProvider.ShareFile.sendLogToEmail(activity)
    }

    // not updated
    override fun clearAllOldLogs(){
        val recordsList = LogProvider.ReadFile.readListFromLogFile(context)
        LogProvider.WriteFile.saveListToLogFile(context, recordsList)
    }

    companion object{
        const val TYPE_EVENT = "EVENT"
    }
}