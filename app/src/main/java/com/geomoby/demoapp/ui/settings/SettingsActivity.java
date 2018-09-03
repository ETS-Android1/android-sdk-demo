package com.geomoby.demoapp.ui.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.geomoby.demoapp.R;

public class SettingsActivity extends MvpAppCompatActivity implements SettingsView {

    @InjectPresenter
    SettingsPresenter mSettingsPresenter;

    private Button mMapStandardButton;
    private Button mMapHybridButton;
    private Button mMapSatelliteButton;
    private ImageView mSettingsBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mMapStandardButton = findViewById(R.id.settingsMap1);
        mMapStandardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSettingsPresenter.setMapModeStandard();
            }
        });

        mMapHybridButton = findViewById(R.id.settingsMap2);
        mMapHybridButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSettingsPresenter.setMapModeHybrid();
            }
        });

        mMapSatelliteButton = findViewById(R.id.settingsMap3);
        mMapSatelliteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSettingsPresenter.setMapModeSatellite();
            }
        });

        mSettingsBackButton = findViewById(R.id.settingsBackButton);
        mSettingsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSettingsPresenter.activityCreated();
    }

    @Override
    public void onSetMapStandard() {
        mMapStandardButton.setBackground(getResources().getDrawable(R.drawable.settings_button_active));
        mMapStandardButton.setTextColor(getResources().getColor(R.color.colorSettingsButtonLight));
        mMapHybridButton.setBackground(getResources().getDrawable(R.drawable.settings_button_inactive));
        mMapHybridButton.setTextColor(getResources().getColor(R.color.colorSettingsButtonDark));
        mMapSatelliteButton.setBackground(getResources().getDrawable(R.drawable.settings_button_inactive));
        mMapSatelliteButton.setTextColor(getResources().getColor(R.color.colorSettingsButtonDark));
    }

    @Override
    public void onSetMapHybrid() {
        mMapStandardButton.setBackground(getResources().getDrawable(R.drawable.settings_button_inactive));
        mMapStandardButton.setTextColor(getResources().getColor(R.color.colorSettingsButtonDark));
        mMapHybridButton.setBackground(getResources().getDrawable(R.drawable.settings_button_active));
        mMapHybridButton.setTextColor(getResources().getColor(R.color.colorSettingsButtonLight));
        mMapSatelliteButton.setBackground(getResources().getDrawable(R.drawable.settings_button_inactive));
        mMapSatelliteButton.setTextColor(getResources().getColor(R.color.colorSettingsButtonDark));
    }

    @Override
    public void onSetMapSatellite() {
        mMapStandardButton.setBackground(getResources().getDrawable(R.drawable.settings_button_inactive));
        mMapStandardButton.setTextColor(getResources().getColor(R.color.colorSettingsButtonDark));
        mMapHybridButton.setBackground(getResources().getDrawable(R.drawable.settings_button_inactive));
        mMapHybridButton.setTextColor(getResources().getColor(R.color.colorSettingsButtonDark));
        mMapSatelliteButton.setBackground(getResources().getDrawable(R.drawable.settings_button_active));
        mMapSatelliteButton.setTextColor(getResources().getColor(R.color.colorSettingsButtonLight));
    }
}
