package com.geomoby.demoapp.ui.settings;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SettingsView extends MvpView {
    void onSetMapStandard();
    void onSetMapHybrid();
    void onSetMapSatellite();
}
