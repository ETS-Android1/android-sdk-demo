package com.geomoby.demoapp.ui.main

import android.app.Application
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geomoby.classes.GeomobyFenceView
import com.geomoby.core.data.data_source.preference_storages.GeomobyDataStorage
import com.geomoby.demoapp.GeoService
import com.geomoby.demoapp.domain.usecases.map_mode.MapModeUseCases
import com.geomoby.demoapp.logic.geomoby.GeoMobyManager
import com.geomoby.demoapp.logic.location.LocationManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModule @Inject constructor(
    val context: Application,
    val mapModeUseCases: MapModeUseCases
):ViewModel(),
    GeoMobyManager.GeoMobyManagerCallback,
    LocationManager.LocationManagerCallback {

    private var locationManager:LocationManager = LocationManager(context)

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: MainActivityEvent) {
        when(event){
            is MainActivityEvent.StartGeomobyManager -> startGeoMobyManager()
            is MainActivityEvent.StartLocationManager -> startLocationManager()
            is MainActivityEvent.StartGeomobyService -> {
                val serviceIntent = Intent(context, GeoService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                } else{
                    context.startService(serviceIntent)
                }
            }
            is MainActivityEvent.StartServiceCheck -> startAlarm(context)

            is MainActivityEvent.GetMapMode -> {
                viewModelScope.launch {
                    _eventFlow.emit(
                        UiEvent.MapModeChanged(mapModeUseCases.getMapMode()))
                }
            }
        }
    }

    private fun startGeoMobyManager() {
        GeoMobyManager.getInstance().setDelegate(this, GeomobyDataStorage(context))
    }

    private fun startLocationManager() {
        locationManager.setDelegate(this)
    }

    override fun onInitLocationChanged(location: Location?) {
            location?.let {
                val latlong = LatLng(location.latitude, location.longitude)
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.InitLocationChanged(latlong))
                }
            }
    }

    override fun onDistanceChanged(distance: String?, inside: Boolean) {
        //TODO Move to SDK
        try {
            val distanceFloat = distance!!.toFloat()
            Log.d("API", "distance changed ui - $distanceFloat")
            val result = Math.round(distanceFloat).toString() + "M"
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.DistanceChanged(result))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onBeaconScanChanged(scanning: Boolean) {
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.BeaconScanChanged(scanning))
        }
    }

    override fun onFenceListChanged(fences: ArrayList<GeomobyFenceView>?) {
        viewModelScope.launch {
            fences?.let {
                _eventFlow.emit(UiEvent.FenceListChanged(it))
            }
        }
    }

    override fun onLocationChanged(location: Location?) {
        viewModelScope.launch {
            location?.let {
                _eventFlow.emit(UiEvent.LocationChanged("${it.latitude} ${it.longitude}"))
            }
        }
    }

    sealed class UiEvent {
        data class InitLocationChanged(val location: LatLng): UiEvent()
        data class DistanceChanged(val distance: String): UiEvent()
        data class BeaconScanChanged(val isScanning:Boolean): UiEvent()
        data class FenceListChanged(val fences: ArrayList<GeomobyFenceView>):UiEvent()
        data class LocationChanged(val location:String):UiEvent()
        data class MapModeChanged(val mode:Int):UiEvent()
    }

}