package com.geomoby.demoapp.ui.fence_history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geomoby.demoapp.R
import com.geomoby.demoapp.data.EventStorage
import java.text.SimpleDateFormat
import java.util.*

class FenceHistoryAdapter(private val list: List<EventStorage.Event>) :
    RecyclerView.Adapter<FenceHistoryAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    class VH(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bind(event:EventStorage.Event){
            itemView.findViewById<TextView>(R.id.textTitle).text = event.title
            itemView.findViewById<TextView>(R.id.textSubtitle).text = event.message
            itemView.findViewById<TextView>(R.id.textTime).text = SimpleDateFormat("dd.MM.yyyy HH:mm").format(
                Date(event.time)
            )
        }
    }
}
