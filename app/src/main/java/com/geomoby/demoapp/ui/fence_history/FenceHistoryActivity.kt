package com.geomoby.demoapp.ui.fence_history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.geomoby.demoapp.R
import com.geomoby.demoapp.databinding.ActivityFenceHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FenceHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFenceHistoryBinding
    private val viewModel:FenceHistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFenceHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvLogData.visibility = View.GONE
        binding.scrollLogs.visibility = View.GONE
        binding.rvMain.visibility = View.VISIBLE
        binding.settingsBackButton.setOnClickListener { finish() }
        binding.settingsNavigationText.setText(R.string.fence_history)

        binding.btnClear.setOnClickListener{
            viewModel.onEvent(FenceHistoryEvent.ClearEventsList)
            viewModel.onEvent(FenceHistoryEvent.GetEventsList)
        }
        binding.btnRefresh.setOnClickListener {
            viewModel.onEvent(FenceHistoryEvent.GetEventsList)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.eventFlow.collect{ event ->
                when(event){
                    is FenceHistoryViewModel.UiEvent.EventListUploaded -> {
                        val list = event.events.sortedWith { o1, o2 -> - o1.time.compareTo(o2.time) }
                        binding.rvMain.adapter = FenceHistoryAdapter(list)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onEvent(FenceHistoryEvent.GetEventsList)
    }
}