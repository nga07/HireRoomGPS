package com.example.finalapplication.screen.searchpost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapplication.databinding.ItemHistoryBinding
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.base.BaseViewHolder

class HistoryAdapter(val itemClick: ItemRecyclerViewListenner<String>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHoler>() {

    private val histories = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHoler {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryBinding.inflate(layoutInflater, parent, false)
        return HistoryViewHoler(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHoler, position: Int) {
        holder.bind(histories[position])
    }

    override fun getItemCount(): Int {
        return histories.size
    }

    fun setData(list: List<String>) {
        histories.clear()
        histories.addAll(list)
        notifyDataSetChanged()
    }

    inner class HistoryViewHoler(val bingding: ItemHistoryBinding) :
        BaseViewHolder<String>(bingding) {
        override fun bind(item: String) {
            bingding.textHistory.text = item
            bingding.root.setOnClickListener {
                itemClick.onItemClick(item)
            }
        }

    }
}
