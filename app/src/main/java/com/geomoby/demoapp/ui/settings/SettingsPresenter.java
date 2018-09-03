package com.geomoby.demoapp.ui.settings;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.geomoby.demoapp.logic.settings.SettingsManager;

@InjectViewState
public class SettingsPresenter extends MvpPresenter<SettingsView> {

    public void activityCreated() {
        int mapMode = SettingsManager.getInstance().getMapMode();
        switch (mapMode) {
            case SettingsManager.MAP_MODE_STANDARD:
                getViewState().onSetMapStandard();
                break;
            case SettingsManager.MAP_MODE_HYBRID:
                getViewState().onSetMapHybrid();
                break;
            case SettingsManager.MAP_MODE_SATELLITE:
                getViewState().onSetMapSatellite();
                break;
        }
    }

    public void setMapModeStandard() {
        SettingsManager.getInstance().setMapMode(SettingsManager.MAP_MODE_STANDARD);
        getViewState().onSetMapStandard();
    }

    public void setMapModeHybrid() {
        SettingsManager.getInstance().setMapMode(SettingsManager.MAP_MODE_HYBRID);
        getViewState().onSetMapHybrid();
    }

    public void setMapModeSatellite() {
        SettingsManager.getInstance().setMapMode(SettingsManager.MAP_MODE_SATELLITE);
        getViewState().onSetMapSatellite();
    }
}