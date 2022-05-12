package com.geomoby.demoapp.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.geomoby.classes.GeomobyFenceView
import com.geomoby.demoapp.GeoService
import com.geomoby.demoapp.R
import com.geomoby.demoapp.data.EventStorage
import com.geomoby.demoapp.data.EventStorageSP
import com.geomoby.demoapp.databinding.ActivityMainBinding
import com.geomoby.demoapp.logic.geomoby.GeoMobyManager
import com.geomoby.demoapp.logic.geomoby.GeoMobyManager.GeoMobyManagerCallback
import com.geomoby.demoapp.logic.location.LocationManager
import com.geomoby.demoapp.logic.location.LocationManager.LocationManagerCallback
import com.geomoby.demoapp.logic.settings.SettingsManager
import com.geomoby.demoapp.ui.fence_history.FenceHistoryActivity
import com.geomoby.demoapp.ui.settings.SettingsActivityNew
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import java.lang.Exception
import java.util.*

class MainActivityNew:AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnMapReadyCallback, GeoMobyManagerCallback,
    LocationManagerCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mMapFragment: SupportMapFragment
    private var mMap: GoogleMap? = null
    lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDrawer()
        mMapFragment = supportFragmentManager.findFragmentById(R.id.mainMapFragment) as SupportMapFragment
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        showProgress()
        askPermissions(this) {
            startApp()
        }
    }

    @SuppressLint("ServiceCast")
    private fun startApp() {
        testGPSError()
        testBatteryOptimization()

        mMapFragment.getMapAsync(this)
        val serviceIntent = Intent(this, GeoService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else{
            startService(serviceIntent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 -> {
                if (isGranted(grantResults)) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                        if (checkBackgroundLocationPermissionAPI30(this)) {
                            Log.e("Permissions", "Permissions Granted new!")
                            startApp()
                        } else {
                            Log.e("Permissions", "Background Location Permissions Not Granted!")
                        }
                    } else {
                        Log.e("Permissions", "Permissions Granted old!")
                        startApp()
                    }
                } else {
                    Toast.makeText(this, "Permission request failed", Toast.LENGTH_LONG).show()
                }
                return
            }
            else -> {}
        }
    }
    private fun setupDrawer() {
        binding.mainNavigationView.setNavigationItemSelectedListener(this)
        binding.mainNavigationView.itemIconTintList = null
        binding.mainMenuButton.setOnClickListener { binding.mainDrawerLayout.openDrawer(GravityCompat.START) }
    }

    private fun showProgress(){
        binding.mainProgress.visibility = View.VISIBLE
    }
    private fun hideProgress(){
        binding.mainProgress.visibility = View.GONE
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> {
                val intent = Intent(this, SettingsActivityNew::class.java)
                startActivity(intent)
            }
            R.id.nav_history -> {
                val intent = Intent(this, FenceHistoryActivity::class.java)
                startActivity(intent)
            }
        }
        binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.uiSettings?.isCompassEnabled = false
        mMap?.uiSettings?.isMyLocationButtonEnabled = false
        mMap?.setMyLocationEnabled(true)

        mMap!!.mapType = when (SettingsManager.instance!!.mapMode) {
            SettingsManager.MAP_MODE_STANDARD -> GoogleMap.MAP_TYPE_NORMAL
            SettingsManager.MAP_MODE_HYBRID -> GoogleMap.MAP_TYPE_HYBRID
            SettingsManager.MAP_MODE_SATELLITE -> GoogleMap.MAP_TYPE_SATELLITE
            else -> GoogleMap.MAP_TYPE_NORMAL
        }

        startGeoMobyManager()
        startLocationManager()
    }

    private fun startGeoMobyManager() {
        GeoMobyManager.instance?.setDelegate(this, this)
    }

    private fun startLocationManager() {
        LocationManager.instance?.setDelegate(this)
    }

    override fun onInitLocationChanged(location: Location?) {
        if (mMap != null) {
            location?.let {
                val latlong = LatLng(location.latitude, location.longitude)
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, 14.0f))
            }
        }
    }

    override fun onDistanceChanged(distance: String?, inside: Boolean) {
        //TODO Move to SDK
        try {
            val distanceFloat = distance!!.toFloat()
            Log.d("API", "distance changed ui - $distanceFloat")
            val result = Math.round(distanceFloat).toString() + "M"
            binding.mainNearestMetersText.text = result
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onBeaconScanChanged(scanning: Boolean) {
        binding.mainBeaconText.text = if (scanning) {
            "Beacons scanning"
        } else {
            "No beacons scanning"
        }
    }

    override fun onFenceListChanged(fences: ArrayList<GeomobyFenceView>?) {
        if(fences == null) return
        hideProgress()
        mMap?.let { map->
            map.clear()
            for (fence in fences) {
                when (fence.type?.lowercase(Locale.ROOT)) {
                    "polygon" -> addPolygon(map, fence)
                    "point" -> addCircle(map, fence)
                    "beacon" -> addBeacon(map, fence)
                    "line" -> addLine(map, fence)
                }
            }
        }
    }

    override fun onLocationChanged(location: Location?) {
        location?.let {
            binding.mainLocationText.text = "${it.latitude} ${it.longitude}"
        }
    }
}