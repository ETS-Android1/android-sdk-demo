package com.geomoby.demoapp.ui.settings;


import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SettingsView extends MvpView {
    void onSetMapStandard();
    void onSetMapHybrid();
    void onSetMapSatellite();
}
