package com.geomoby.demoapp.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import gr.net.maroulis.library.EasySplashScreen
import com.geomoby.demoapp.R
import com.geomoby.demoapp.ui.main.MainActivityNew

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val easySplashScreenView = EasySplashScreen(this@SplashActivity)
            .withFullScreen()
            .withTargetActivity(MainActivityNew::class.java)
            .withSplashTimeOut(3000)
            .withBackgroundResource(R.mipmap.splash)
            .create()
        setContentView(easySplashScreenView)
    }
}