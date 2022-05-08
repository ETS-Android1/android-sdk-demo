package com.geomoby.demoapp.ui.settings

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.geomoby.demoapp.R
import com.geomoby.demoapp.databinding.ActivitySettingsBinding
import com.geomoby.demoapp.logic.settings.SettingsManager

class SettingsActivityNew: AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsMap1.setOnClickListener { setMapModeStandard() }
        binding.settingsMap2.setOnClickListener { setMapModeHybrid() }
        binding.settingsMap3.setOnClickListener { setMapModeSatellite() }

        val mSettingsBackButton = findViewById<ImageView>(R.id.settingsBackButton)
        mSettingsBackButton.setOnClickListener { finish() }

        when (SettingsManager.instance!!.mapMode) {
            SettingsManager.MAP_MODE_STANDARD -> onSetMapStandard()
            SettingsManager.MAP_MODE_HYBRID -> onSetMapHybrid()
            SettingsManager.MAP_MODE_SATELLITE -> onSetMapSatellite()
        }
    }
    private fun setMapModeStandard() {
        SettingsManager.instance!!.mapMode = SettingsManager.MAP_MODE_STANDARD
        onSetMapStandard()
    }

    private fun setMapModeHybrid() {
        SettingsManager.instance!!.mapMode = SettingsManager.MAP_MODE_HYBRID
        onSetMapHybrid()
    }

    private fun setMapModeSatellite() {
        SettingsManager.instance!!.mapMode = SettingsManager.MAP_MODE_SATELLITE
        onSetMapSatellite()
    }

    private fun onSetMapStandard() {
        binding.settingsMap1.background = resources.getDrawable(R.drawable.settings_button_active)
        binding.settingsMap1.setTextColor(resources.getColor(R.color.colorSettingsButtonLight))
        binding.settingsMap2.background = resources.getDrawable(R.drawable.settings_button_inactive)
        binding.settingsMap2.setTextColor(resources.getColor(R.color.colorSettingsButtonDark))
        binding.settingsMap3.background = resources.getDrawable(R.drawable.settings_button_inactive)
        binding.settingsMap3.setTextColor(resources.getColor(R.color.colorSettingsButtonDark))
    }

    private fun onSetMapHybrid() {
        binding.settingsMap1.background = resources.getDrawable(R.drawable.settings_button_inactive)
        binding.settingsMap1.setTextColor(resources.getColor(R.color.colorSettingsButtonDark))
        binding.settingsMap2.background = resources.getDrawable(R.drawable.settings_button_active)
        binding.settingsMap2.setTextColor(resources.getColor(R.color.colorSettingsButtonLight))
        binding.settingsMap3.background = resources.getDrawable(R.drawable.settings_button_inactive)
        binding.settingsMap3.setTextColor(resources.getColor(R.color.colorSettingsButtonDark))
    }

    private fun onSetMapSatellite() {
        binding.settingsMap1.background = resources.getDrawable(R.drawable.settings_button_inactive)
        binding.settingsMap1.setTextColor(resources.getColor(R.color.colorSettingsButtonDark))
        binding.settingsMap2.background = resources.getDrawable(R.drawable.settings_button_inactive)
        binding.settingsMap2.setTextColor(resources.getColor(R.color.colorSettingsButtonDark))
        binding.settingsMap3.background = resources.getDrawable(R.drawable.settings_button_active)
        binding.settingsMap3.setTextColor(resources.getColor(R.color.colorSettingsButtonLight))
    }
}