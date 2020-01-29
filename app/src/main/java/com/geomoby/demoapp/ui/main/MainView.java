package com.geomoby.demoapp.ui.main;

import com.geomoby.classes.GeomobyFenceView;
import com.karumi.dexter.PermissionToken;

import java.util.ArrayList;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MainView extends MvpView {
    //void onCheckPermissionRationale(final String permission, int requestCode);
   // void onShowRationaleDialog(final String permission, final int requestCode, String text);
    //void onRequestForPermissions(String[] permissions, int requestCode);
    void showPermissionRationale(PermissionToken token);
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