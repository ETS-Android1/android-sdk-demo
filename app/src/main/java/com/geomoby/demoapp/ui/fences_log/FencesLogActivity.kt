package com.geomoby.demoapp.ui.fences_log

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.geomoby.demoapp.R
import com.geomoby.demoapp.data.EventStorage
import com.geomoby.demoapp.data.EventStorageSP
import com.geomoby.demoapp.data.ExperimentsLogger
import com.geomoby.demoapp.databinding.ActivityFenceHistoryBinding
import com.geomoby.demoapp.databinding.ActivityMainBinding
import com.geomoby.demoapp.databinding.ActivitySettingsBinding
import kotlinx.android.synthetic.main.activity_fence_history.*
import java.util.*
import kotlin.Comparator

class FencesLogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFenceHistoryBinding
    private val timer:Timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFenceHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.settingsBackButton.setOnClickListener { finish() }
        binding.settingsNavigationText.setText(R.string.fences_log)
        binding.btnClear.setOnClickListener{
            //EventStorageSP(this).clearEventsList()
            //initAdapter()
            ExperimentsLogger(this).clearAllLogs()
            initLogText()
        }
        binding.btnRefresh.setOnClickListener {
            //initAdapter()
            initLogText()
        }
        binding.btnSendToEmail.setOnClickListener {
            ExperimentsLogger(this).sendLog(this)
        }
    }

    override fun onStart() {
        super.onStart()
        //initAdapter()
        initLogText()
        /*timer.scheduleAtFixedRate(object:TimerTask(){
            override fun run() {
                updateHandler.sendEmptyMessage(1)
            }
        }, 0L, REFRESH_PERIOD)*/
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }

    private val updateHandler = object: Handler(){
        override fun dispatchMessage(msg: Message) {
            //initAdapter()
        }
    }

    private fun initLogText(){
        binding.tvLogData.text = ExperimentsLogger(this).getAllLogData()
    }

    companion object{
        const val REFRESH_PERIOD = 10_000L
    }
}