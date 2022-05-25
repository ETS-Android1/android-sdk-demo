package com.geomoby.demoapp.ui.fences_log

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.geomoby.demoapp.R
import com.geomoby.demoapp.data.event_logger.EventLoggerFile
import com.geomoby.demoapp.databinding.ActivityFenceHistoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class FencesLogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFenceHistoryBinding
    private val timer:Timer = Timer()
    private val viewModel:FencesLogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFenceHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //viewModel.onEvent(FenceLogEvent.ClearLog)

        binding.settingsBackButton.setOnClickListener { finish() }
        binding.settingsNavigationText.setText(R.string.fences_log)
        binding.btnClear.setOnClickListener{
            viewModel.onEvent(FenceLogEvent.ClearLog)
            viewModel.onEvent(FenceLogEvent.GetFenceLog)
        }

        binding.btnRefresh.setOnClickListener {
            viewModel.onEvent(FenceLogEvent.GetFenceLog)
        }

        binding.btnSendToEmail.setOnClickListener {
            viewModel.onEvent(FenceLogEvent.SendLog(this))
        }

        lifecycleScope.launchWhenCreated {
            viewModel.eventFlow.collect{ event ->
                when(event){
                    is FencesLogViewModel.UiEvent.LogsUploaded -> {
                        event.logStr?.let{
                            binding.tvLogData.text = it
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onEvent(FenceLogEvent.GetFenceLog)
    }
}