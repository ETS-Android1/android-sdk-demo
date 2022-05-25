package com.geomoby.demoapp.domain.repositories

import com.google.gson.annotations.SerializedName

interface EventStorage{
    data class Event(
        @SerializedName("title") val title:String,
        @SerializedName("message") val message:String,
        @SerializedName("time") val time:Long = System.currentTimeMillis()
    )

    fun addEvent(event: Event)
    fun getEventsList():List<Event>
    fun clearEventsList()
}