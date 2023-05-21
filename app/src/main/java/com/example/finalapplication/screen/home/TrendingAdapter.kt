package com.example.finalapplication.screen.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapplication.databinding.ItemTrendingBinding
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.base.BaseViewHolder

class TrendingAdapter(val itemclick: ItemRecyclerViewListenner<Any>) :
    RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder>() {

    private val items = mutableListOf<Pair<String, Int>>()

    fun setData(list: List<Pair<String, Int>>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTrendingBinding.inflate(layoutInflater, parent, false)
        return TrendingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrendingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class TrendingViewHolder(val binding: ItemTrendingBinding) :
        BaseViewHolder<Pair<String, Int>>(binding) {
        override fun bind(item: Pair<String, Int>) {
            binding.imageDistrict.setImageResource(item.second)
            binding.textName.text = item.first
            binding.root.setOnClickListener {
                itemclick.onItemClick(item.first)
            }
        }

    }
}