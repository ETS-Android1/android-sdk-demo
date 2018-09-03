package com.geomoby.demoapp.ui.splash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.geomoby.demoapp.R;
import com.geomoby.demoapp.ui.main.MainActivity;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View easySplashScreenView = new EasySplashScreen(SplashActivity.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(3000)
                .withBackgroundResource(R.mipmap.splash)
                .create();

        setContentView(easySplashScreenView);
    }
}
