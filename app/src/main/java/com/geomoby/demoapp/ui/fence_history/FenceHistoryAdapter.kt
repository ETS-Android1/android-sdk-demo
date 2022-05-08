package com.geomoby.demoapp.ui.fence_history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geomoby.demoapp.data.EventStorage

class FenceHistoryAdapter(private val list: List<EventStorage.Event>) :
    RecyclerView.Adapter<FenceHistoryAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    class VH(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bind(event:EventStorage.Event){
            itemView.findViewById<TextView>(android.R.id.text1).text = event.title
            itemView.findViewById<TextView>(android.R.id.text2).text = event.message
        }
    }
}
