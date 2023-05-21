package com.example.finalapplication.screen.createpostultility

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapplication.R
import com.example.finalapplication.data.model.Utility
import com.example.finalapplication.databinding.ItemUtilityBinding
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.base.BaseViewHolder

class UtilityAdapter(private val itemclick: ItemRecyclerViewListenner<Utility>) :
    RecyclerView.Adapter<UtilityAdapter.UtilityViewHolder>() {

    private val utilities = mutableListOf<Utility>()
    private val mapCheck = mutableMapOf<Int, Boolean>()

    fun setData(list: List<Utility>) {
        utilities.clear()
        utilities.addAll(list)
        for (i in list) {
            i.id?.let { mapCheck.put(it, false) }
        }
    }

    fun addData(list: List<Utility>){
        for(uti in list){
            if(mapCheck[uti.id] == false) mapCheck[uti.id ?: 0] = true
        }
        notifyDataSetChanged()
    }

    inner class UtilityViewHolder(val binding: ItemUtilityBinding) :
        BaseViewHolder<Utility>(binding) {
        init {
            binding.root.setOnClickListener {
                itemclick.onItemClick(utilities[adapterPosition])
                val id = utilities[adapterPosition].id
                id?.let {
                    if (mapCheck[id] == true) {
                        mapCheck[id] = false
                        binding.containerUtility.setBackgroundResource(R.color.Gray)
                    } else {
                        binding.containerUtility.setBackgroundResource(R.drawable.bg_border_15dp)
                        mapCheck[id] = true
                    }
                }

            }
        }

        override fun bind(item: Utility) {
            item.icon?.let { binding.imgeIcon.setImageResource(it) }
            binding.textName.text = item.name
            val id = item.id
            id?.let {
                if (mapCheck[id] == true) {
                    binding.containerUtility.setBackgroundResource(R.drawable.bg_border_15dp)
                } else {
                    binding.containerUtility.setBackgroundResource(R.color.Gray)
                }
            }
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
