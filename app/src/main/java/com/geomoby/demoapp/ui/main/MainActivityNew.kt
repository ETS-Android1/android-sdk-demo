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
import com.geomoby.demoapp.ui.fences_log.FencesLogActivity
import com.geomoby.demoapp.ui.settings.SettingsActivityNew
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.util.*
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope

@AndroidEntryPoint
class MainActivityNew:AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    OnMapReadyCallback
{

    private lateinit var binding: ActivityMainBinding
    private lateinit var mMapFragment: SupportMapFragment
    private var mMap: GoogleMap? = null
    lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    private val viewModel: MainActivityViewModule by viewModels()

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
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect{
                when(it){
                    is MainActivityViewModule.UiEvent.InitLocationChanged -> {
                        if (mMap != null) {
                            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(it.location, 14.0f))
                        }
                    }
                    is MainActivityViewModule.UiEvent.DistanceChanged -> {
                        binding.mainNearestMetersText.text = it.distance
                    }
                    is MainActivityViewModule.UiEvent.BeaconScanChanged -> {
                        binding.mainBeaconText.text = if (it.isScanning) {
                            "Beacons scanning"
                        } else {
                            "No beacons scanning"
                        }
                    }
                    is MainActivityViewModule.UiEvent.FenceListChanged -> {
                        hideProgress()
                        mMap?.let { map->
                            map.clear()
                            for (fence in it.fences) {
                                when (fence.type?.lowercase(Locale.ROOT)) {
                                    "polygon" -> addPolygon(map, fence)
                                    "point" -> addCircle(map, fence)
                                    "beacon" -> addBeacon(map, fence)
                                    "line" -> addLine(map, fence)
                                }
                            }
                        }
                    }
                    is MainActivityViewModule.UiEvent.LocationChanged -> {
                        binding.mainLocationText.text = it.location
                    }
                }
            }
        }
    }

    @SuppressLint("ServiceCast")
    private fun startApp() {
        testGPSError()
        testBatteryOptimization()

        mMapFragment.getMapAsync(this)

        viewModel.onEvent(MainActivityEvent.StartGeomobyService)
        val serviceIntent = Intent(this, GeoService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(serviceIntent)
        } else{
            this.startService(serviceIntent)
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
            R.id.nav_logs -> {
                val intent = Intent(this, FencesLogActivity::class.java)
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
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap?.isMyLocationEnabled = true

        mMap!!.mapType = when (SettingsManager(this).mapMode) {
            SettingsManager.MAP_MODE_STANDARD -> GoogleMap.MAP_TYPE_NORMAL
            SettingsManager.MAP_MODE_HYBRID -> GoogleMap.MAP_TYPE_HYBRID
            SettingsManager.MAP_MODE_SATELLITE -> GoogleMap.MAP_TYPE_SATELLITE
            else -> GoogleMap.MAP_TYPE_NORMAL
        }
        viewModel.onEvent(MainActivityEvent.StartGeomobyManager)
        viewModel.onEvent(MainActivityEvent.StartLocationManager)
    }
}