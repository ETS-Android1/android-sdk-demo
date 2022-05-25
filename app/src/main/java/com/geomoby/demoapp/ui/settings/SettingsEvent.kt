package com.geomoby.demoapp.ui.settings

sealed class SettingsEvent {
    class SetMapMode(val mode:Int):SettingsEvent()
    object GetMapMode:SettingsEvent()
}