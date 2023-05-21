package com.example.finalapplication.screen.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.databinding.ItemLoadMoreBinding
import com.example.finalapplication.databinding.ItemNewRoomBinding
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.base.BaseViewHolder
import com.example.finalapplication.utils.loadImageByUrl

class VicintyPostAdapter(val itemClick: ItemRecyclerViewListenner<Post>) :
    RecyclerView.Adapter<VicintyPostAdapter.PostViewHolder>() {

    private val currentList = mutableListOf<Pair<Post, Double>>()

    fun setData(list : List<Pair<Post,Double>>){
        currentList.clear()
        currentList.addAll(list)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PostViewHolder(ItemNewRoomBinding.inflate(layoutInflater, parent, false))
    }

    inner class PostViewHolder(val binding: ItemNewRoomBinding) : BaseViewHolder<Pair<Post, Double>>(binding) {

        override fun bind(item: Pair<Post, Double>) {
            val post = item.first
            binding.apply {
                textDistance.isVisible = true
                textDistance.text = String.format("%.2f", item.second)+"Km"
                root.setOnClickListener {
                    itemClick.onItemClick(post)
                }
                imageVerify.isVisible = post.verify == true
                imageRoom.loadImageByUrl(post.images?.get(0), root.context)
                textPrice.text = "${post.price} VND"
                textType.text = post.postType?.toUpperCase()
                textAddress.text = post.address.toString()
                textTitle.text = post.title
            }
        }

    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

}
