package com.geomoby.demoapp.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

interface EventStorage{
    data class Event(
        @SerializedName("title") val title:String,
        @SerializedName("message") val message:String,
        @SerializedName("time") val time:Long = System.currentTimeMillis()
    )

    fun addEvent(event:Event)
    fun getEventsList():List<Event>
    fun clearEventsList()
}

class EventStorageSP(context: Context):EventStorage {
    private val sharedPreferences = context.getSharedPreferences(SETTINGS_KEY, Context.MODE_PRIVATE)
    private val gson = Gson()

    private fun getEventSet() = sharedPreferences.getStringSet(EVENTS_KEY, emptySet())?: emptySet()

    override fun addEvent(event: EventStorage.Event) {
        val events = getEventSet().plusElement(gson.toJson(event).toString())
        sharedPreferences.edit().putStringSet(EVENTS_KEY,events).apply()
    }

    override fun getEventsList() = getEventSet().map { item->
        gson.fromJson(item,EventStorage.Event::class.java)
    }.toList()

    override fun clearEventsList() {
        sharedPreferences.edit().putStringSet(EVENTS_KEY, emptySet()).apply()
    }

    companion object{
        private const val SETTINGS_KEY = "GeoMobySettings"
        private const val EVENTS_KEY = "event"
    }
}