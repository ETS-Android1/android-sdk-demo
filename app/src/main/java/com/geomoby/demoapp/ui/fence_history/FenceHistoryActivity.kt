package com.geomoby.demoapp.ui.fence_history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.geomoby.demoapp.R
import com.geomoby.demoapp.data.EventStorage
import com.geomoby.demoapp.data.EventStorageSP
import com.geomoby.demoapp.databinding.ActivityFenceHistoryBinding
import com.geomoby.demoapp.databinding.ActivityMainBinding
import com.geomoby.demoapp.databinding.ActivitySettingsBinding
import kotlinx.android.synthetic.main.activity_fence_history.*
import java.util.*
import kotlin.Comparator

class FenceHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFenceHistoryBinding
    private val timer:Timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFenceHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnClear.setOnClickListener{
            EventStorageSP(this).clearEventsList()
            initAdapter()
        }
        binding.btnRefresh.setOnClickListener {
            initAdapter()
        }
    }

    override fun onStart() {
        super.onStart()
        initAdapter()
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
            initAdapter()
        }
    }

    private fun initAdapter(){
        val list = EventStorageSP(this).getEventsList().sortedWith { o1, o2 -> - o1.time.compareTo(o2.time) }
        rvMain.adapter = FenceHistoryAdapter(list)
    }

    companion object{
        const val REFRESH_PERIOD = 10_000L
    }
}