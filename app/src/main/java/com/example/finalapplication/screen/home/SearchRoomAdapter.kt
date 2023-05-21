package com.example.finalapplication.screen.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.User
import com.example.finalapplication.databinding.ItemLoadMoreBinding
import com.example.finalapplication.databinding.ItemSearchRoomBinding
import com.example.finalapplication.screen.searchpost.ResultPostAdapter
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.base.BaseViewHolder
import com.example.finalapplication.utils.loadImageByUrl

class SearchRoomAdapter(val itemclick: ItemRecyclerViewListenner<Any>) :
    RecyclerView.Adapter<BaseViewHolder<Any>>() {

    var haveNextPage = true
    private val list = mutableListOf<Pair<Post, User>>()

    fun setData(items: List<Pair<Post, User>>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (list.size > 0 && position == list.size && haveNextPage) return ResultPostAdapter.TYPE_LOAD
        return ResultPostAdapter.TYPE_DATA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Any> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_LOAD -> LoadingViewHolder(
                ItemLoadMoreBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
            else -> SearchRoomViewHolder(
                ItemSearchRoomBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Any>, position: Int) {
        if (list.size <= position) return
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        if (haveNextPage && list.size > 0) return list.size + 1
        return list.size
    }

    inner class SearchRoomViewHolder(val binding: ItemSearchRoomBinding) :
        BaseViewHolder<Any>(binding) {

        override fun bind(item: Any) {
            item as Pair<Post, User>
            binding.apply {
                root.setOnClickListener {
                    itemclick.onItemClick(item)
                }
                buttonOption.isVisible = false
                imageAvatar.loadImageByUrl(item.second.avatar, root.context)
                textName.text = item.second.name
                textAddress.text = item.first.address.district + ", " + item.first.address.city
                textPrice.text = "${item.first.minPrice} VND - ${item.first.maxPrice} VND"
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