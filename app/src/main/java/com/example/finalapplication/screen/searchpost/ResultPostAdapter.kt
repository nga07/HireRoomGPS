package com.example.finalapplication.screen.searchpost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.databinding.ItemLoadMoreBinding
import com.example.finalapplication.databinding.ItemNewRoomBinding
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.base.BaseViewHolder
import com.example.finalapplication.utils.loadImageByUrl

class ResultPostAdapter(val itemClick: ItemRecyclerViewListenner<Post>) :
    ListAdapter<Post, BaseViewHolder<Any>>(Post.diffCallback) {

    var haveNextPage = true

    override fun getItemViewType(position: Int): Int {
        if (currentList.size > 0 && position == currentList.size && haveNextPage) return TYPE_LOAD
        return TYPE_DATA
    }

    override fun getItemCount(): Int {
        if (haveNextPage && currentList.size > 0) return currentList.size + 1
        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Any> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_LOAD ->
                LoadingViewHolder(ItemLoadMoreBinding.inflate(layoutInflater, parent, false))
            else ->
                PostViewHolder(ItemNewRoomBinding.inflate(layoutInflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Any>, position: Int) {
        if (currentList.size <= position) return
        holder.bind(getItem(position))
    }

    inner class PostViewHolder(val binding: ItemNewRoomBinding) : BaseViewHolder<Any>(binding) {

        override fun bind(item: Any) {
            val post = item as Post
            binding.apply {
                root.setOnClickListener {
                    itemClick.onItemClick(post)
                }
                imageVerify.isVisible = item.verify==true
                imageRoom.loadImageByUrl(post.images?.get(0), root.context)
                textPrice.text = "${post.price} VND"
                textType.text = post.postType
                textAddress.text = post.address.toString()
                textTitle.text = post.title
            }
        }

    }

    inner class LoadingViewHolder(val binding: ItemLoadMoreBinding) : BaseViewHolder<Any>(binding) {

        override fun bind(item: Any) {
            //TODO("Not yet implemented")
        }
    }

    companion object {
        const val TYPE_DATA = 1
        const val TYPE_LOAD = 2
    }
}
