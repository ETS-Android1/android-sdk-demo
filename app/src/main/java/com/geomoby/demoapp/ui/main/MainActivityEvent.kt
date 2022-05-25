package com.geomoby.demoapp.ui.main

sealed class MainActivityEvent{
    object StartGeomobyManager:MainActivityEvent()
    object StartLocationManager:MainActivityEvent()
    object StartGeomobyService:MainActivityEvent()
    object StartServiceCheck:MainActivityEvent()
    object GetMapMode:MainActivityEvent()
}