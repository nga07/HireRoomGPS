package com.example.finalapplication.screen.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.finalapplication.data.model.User
import com.example.finalapplication.databinding.ItemLoadMoreBinding
import com.example.finalapplication.databinding.ItemUserBinding
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.base.BaseViewHolder
import com.example.finalapplication.utils.loadImageByUrl

class SearchListAdapter(private val itemClickListenner: ItemRecyclerViewListenner<User>) :
    ListAdapter<User, BaseViewHolder<Any>>(User.diffCallBack) {

    var haveNextPage = true

    override fun getItemViewType(position: Int): Int {
        if (currentList.size > 0 && position == currentList.size - 1 && haveNextPage) return TYPE_LOAD
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
                LoadViewHolder(ItemLoadMoreBinding.inflate(layoutInflater, parent, false))
            else ->
                UserViewHolder(ItemUserBinding.inflate(layoutInflater, parent, false))
        }
    }

    inner class LoadViewHolder(val binding: ItemLoadMoreBinding) : BaseViewHolder<Any>(binding) {

        override fun bind(item: Any) {
            // TODO no-ip
        }
    }

    inner class UserViewHolder(val binding: ItemUserBinding) : BaseViewHolder<Any>(binding) {

        override fun bind(item: Any) {

            binding.apply {
                val user = item as User
                root.setOnClickListener {
                    itemClickListenner.onItemClick(user)
                }
                textUsername.text = user.name
                circleAvatar.loadImageByUrl(user.avatar, root.context)
            }
        }
    }

    companion object {
        const val TYPE_DATA = 1
        const val TYPE_LOAD = 2
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Any>, position: Int) {
        if (currentList.size <= position) return
        holder.bind(getItem(position))
    }
}
