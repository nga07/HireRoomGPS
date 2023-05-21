package com.example.finalapplication.screen.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.databinding.ItemNewRoomBinding
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.base.BaseViewHolder
import com.example.finalapplication.utils.loadImageByUrl

class NewRoomAdapter(val itemClick: ItemRecyclerViewListenner<Any>) :
    RecyclerView.Adapter<NewRoomAdapter.NewRoomViewHolder>() {

    private val posts = mutableListOf<Post>()

    fun setData(posts: List<Post>) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewRoomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemNewRoomBinding.inflate(layoutInflater, parent, false)
        return NewRoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewRoomViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }


    inner class NewRoomViewHolder(val binding: ItemNewRoomBinding) : BaseViewHolder<Post>(binding) {
        override fun bind(item: Post) {
            binding.apply {
                root.setOnClickListener {
                    itemClick.onItemClick(item)
                }
                imageVerify.isVisible = !(item.verify == null || item.verify == false)
                imageRoom.loadImageByUrl(item.images?.first(), root.context)
                textType.text = item.postType?.toUpperCase()
                textPrice.text = item.price.toString() + " VND"
                textAddress.text = item.address.toString()
                textTitle.text = item.title
            }
        }
    }
}