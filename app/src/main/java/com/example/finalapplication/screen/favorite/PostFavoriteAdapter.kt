package com.example.finalapplication.screen.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapplication.R
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.databinding.ItemRoomateBinding
import com.example.finalapplication.utils.FavoriteListener
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.base.BaseViewHolder
import com.example.finalapplication.utils.loadImageByUrl

class PostFavoriteAdapter(
    val itemClick: ItemRecyclerViewListenner<Any>,
    val favoriteClick: FavoriteListener
) : RecyclerView.Adapter<PostFavoriteAdapter.RoomateViewHolder>() {

    private val posts = mutableListOf<Post>()

    fun setData(posts: List<Post>) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRoomateBinding.inflate(layoutInflater, parent, false)
        return RoomateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoomateViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class RoomateViewHolder(val binding: ItemRoomateBinding) : BaseViewHolder<Post>(binding) {
        override fun bind(item: Post) {
            binding.apply {
                imageFavorite.isVisible = true
                imageFavorite.setImageResource(R.drawable.icon_favotite_24)
                root.setOnClickListener {
                    itemClick.onItemClick(item)
                }
                imageFavorite.setOnClickListener {
                    favoriteClick.onclick(item)
                }
                imageRoom.loadImageByUrl(item.images?.first(), root.context)
                imageVerify.isVisible = !(item.verify == null || item.verify == false)
                textTitle.text = item.title
                textPrice.text = "${item.price} VND"
                textAddress.text = item.address.toString()
                textType.text = item.postType?.toUpperCase()
            }
        }

    }
}