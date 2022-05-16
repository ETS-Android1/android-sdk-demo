package com.geomoby.demoapp.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.geomoby.demoapp.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.lang.ClassCastException
import androidx.core.content.ContextCompat.startActivity

import android.content.Context.POWER_SERVICE

import androidx.core.content.ContextCompat.getSystemService

import android.os.PowerManager

import android.content.Intent
import android.net.Uri
import android.provider.Settings


val permissions =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACTIVITY_RECOGNITION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACTIVITY_RECOGNITION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
        else -> {
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

@RequiresApi(Build.VERSION_CODES.M)
internal fun AppCompatActivity.askPermissions(
    activity:AppCompatActivity,
    onPermissionConfirmed:()->Unit) {
    when {
        checkPermissions(this) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (checkBackgroundLocationPermissionAPI30(this)) {
                    Log.e("Permissions", "Permissions Granted new!")
                    onPermissionConfirmed()
                } else {
                    Log.e("Permissions", "Background Location Permissions Not Granted!")
                }
            } else {
                Log.e("Permissions", "Permissions Granted old!")
                onPermissionConfirmed()
            }
        }
        !checkShouldShowRationale(this)-> {
            showPermissionRationale(this)
        }
        else -> {
            activity.requestPermissions(permissions, 101)
        }
    }

}

internal fun checkBackgroundLocationPermissionAPI30(activity: Activity): Boolean {
    return if (ActivityCompat.checkSelfPermission(
            activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        androidx.appcompat.app.AlertDialog.Builder(activity)
            .setMessage(R.string.always_allow)
            .setPositiveButton(R.string.open_settings) { dialogInterface, i ->
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    101
                )
            }
            .setNegativeButton(R.string.ok) { dialogInterface, i -> }
            .create()
            .show()
        false
    } else {
        true
    }
}

private fun showPermissionRationale(context: Context) {
    AlertDialog.Builder(context).setTitle(R.string.permission_rationale_title)
        .setMessage(R.string.permission_rationale_message)
        .setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        .setPositiveButton(R.string.ok) { dialog, _ ->
            dialog.dismiss()
        }
        .setOnDismissListener {}
        .show()
}

fun checkPermissions(context: Context):Boolean{
    return permissions.none { permission ->
        ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun checkShouldShowRationale(activity: AppCompatActivity):Boolean{
    return permissions.none { permission ->
        activity.shouldShowRequestPermissionRationale(permission)
    }
}

fun isGranted(grantResults: IntArray):Boolean{
    return grantResults.none { result ->
        result != PackageManager.PERMISSION_GRANTED
    }
}

internal fun AppCompatActivity.testGPSError() {
    val mLocationRequestHighAccuracy = LocationRequest()
    mLocationRequestHighAccuracy.interval = 500
    mLocationRequestHighAccuracy.fastestInterval = 500
    mLocationRequestHighAccuracy.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    val builder = LocationSettingsRequest.Builder()
        .addLocationRequest(mLocationRequestHighAccuracy)
    val result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
    result.addOnCompleteListener { task: Task<LocationSettingsResponse?> ->
        try {
            val response =
                task.getResult(ApiException::class.java)
            // All location settings are satisfied. The client can initialize location
            // requests here.
        } catch (exception: ApiException) {
            when (exception.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                    // Location settings are not satisfied. But could be fixed by showing the
                    // user a dialog.
                    try {
                        // Cast to a resolvable exception.
                        val resolvable = exception as ResolvableApiException
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        resolvable.startResolutionForResult(
                            this,
                            121
                        )
                    } catch (e: SendIntentException) {
                        // Ignore the error.
                    } catch (e: ClassCastException) {
                        // Ignore, should be an impossible error.
                    }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }
        }
    }
}

@SuppressLint("BatteryLife")
fun AppCompatActivity.testBatteryOptimization(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val intent = Intent()
        getSystemService(this,PowerManager::class.java)?.let {
            if (!it.isIgnoringBatteryOptimizations(packageName)) {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                startActivity(this, intent, null)
            }
        }

    }
}

private fun Context.buildAlertMessageNoGps() {
    val builder = AlertDialog.Builder(this)
    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
        .setCancelable(false)
        .setPositiveButton(
            "Yes"
        ) { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
        .setNegativeButton(
            "No"
        ) { dialog, id -> dialog.cancel() }
    val alert = builder.create()
    alert.show()
}