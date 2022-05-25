package com.geomoby.demoapp.ui.settings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.geomoby.demoapp.R
import com.geomoby.demoapp.databinding.ActivitySettingsBinding
import com.geomoby.demoapp.domain.repositories.MapMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivityNew: AppCompatActivity() {

    private val viewModel: SettingsActivityViewModel by viewModels()

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsMap1.setOnClickListener { setMapModeStandard() }
        binding.settingsMap2.setOnClickListener { setMapModeHybrid() }
        binding.settingsMap3.setOnClickListener { setMapModeSatellite() }
        binding.settingsBackButton.setOnClickListener { finish() }

        lifecycleScope.launchWhenCreated {
            viewModel.eventFlow.collect{ event->
                when(event){
                    is SettingsActivityViewModel.UiEvent.MapModeStandard -> onSetMapStandard()
                    is SettingsActivityViewModel.UiEvent.MapModeSatellite -> onSetMapSatellite()
                    is SettingsActivityViewModel.UiEvent.MapModeHybrid -> onSetMapHybrid()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onEvent(SettingsEvent.GetMapMode)
    }

    private fun setMapModeStandard() {
        viewModel.onEvent(SettingsEvent.SetMapMode(MapMode.MAP_MODE_STANDARD))
    }

    private fun setMapModeHybrid() {
        viewModel.onEvent(SettingsEvent.SetMapMode(MapMode.MAP_MODE_HYBRID))
    }

    private fun setMapModeSatellite() {
        viewModel.onEvent(SettingsEvent.SetMapMode(MapMode.MAP_MODE_SATELLITE))
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