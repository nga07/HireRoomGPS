package com.example.finalapplication.screen.postdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapplication.R
import com.example.finalapplication.data.model.Utility
import com.example.finalapplication.databinding.ItemUtilityBinding
import com.example.finalapplication.utils.base.BaseViewHolder

class PDUtilityAdapter() :
    RecyclerView.Adapter<PDUtilityAdapter.UtilityViewHolder>() {

    private val utilities = mutableListOf<Utility>()

    fun setData(list: List<Utility>) {
        utilities.clear()
        utilities.addAll(list)
    }

    inner class UtilityViewHolder(val binding: ItemUtilityBinding) :
        BaseViewHolder<Utility>(binding) {
        init {
            binding.containerUtility.setBackgroundResource(R.drawable.bg_border_15dp)
        }

        override fun bind(item: Utility) {
            item.icon?.let { binding.imgeIcon.setImageResource(it) }
            binding.textName.text = item.name
            R.drawable.ic_balcony_35
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UtilityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemUtilityBinding.inflate(layoutInflater, parent, false)
        return UtilityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UtilityViewHolder, position: Int) {
        holder.bind(utilities[position])
    }

    override fun getItemCount(): Int {
        return utilities.size
    }
}