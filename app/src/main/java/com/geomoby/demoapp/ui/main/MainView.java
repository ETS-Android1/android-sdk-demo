package com.geomoby.demoapp.ui.main;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.geomoby.classes.GeomobyFenceView;

import java.util.ArrayList;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MainView extends MvpView {
    void onCheckPermissionRationale(final String permission, int requestCode);
    void onShowRationaleDialog(final String permission, final int requestCode, String text);
    void onRequestForPermissions(String[] permissions, int requestCode);
    void onShowProgress(boolean show);
    void onStartMap();
    void onSetMapModeStandard();
    void onSetMapModeHybrid();
    void onSetMapModeSatellite();
    void onInitLocationChanged(double latitude, double longitude);
    void onLocationChanged(double latitude, double longitude);
    void onDistanceChanged(String distance);
    void onBeaconScanChanged(boolean scanning);
    void onFenceListChanged(ArrayList<GeomobyFenceView> fences);
}