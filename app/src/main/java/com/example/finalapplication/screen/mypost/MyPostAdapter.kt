package com.example.finalapplication.screen.mypost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.User
import com.example.finalapplication.databinding.ItemNewRoomBinding
import com.example.finalapplication.databinding.ItemSearchRoomBinding
import com.example.finalapplication.utils.ItemOptionClickListenner
import com.example.finalapplication.utils.base.BaseViewHolder
import com.example.finalapplication.utils.loadImageByUrl

class MyPostAdapter(val optionClick : ItemOptionClickListenner) : RecyclerView.Adapter<BaseViewHolder<Post>>(){

    private val posts = mutableListOf<Post>()
    private var user : User? = null

    fun setUser(user : User){
        this.user = user
    }

    fun setData(posts : List<Post>){
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if(posts[position].postType == Post.timPhongO) return POST_SEARCH
        else return POST_REND
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Post> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType){
            POST_REND -> PostRendViewHolder(ItemNewRoomBinding.inflate(layoutInflater, parent, false))
            else -> PostSearchViewHolder(ItemSearchRoomBinding.inflate(layoutInflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Post>, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }


    inner class PostRendViewHolder(val binding : ItemNewRoomBinding) : BaseViewHolder<Post>(binding){

        init {
            binding.apply {
                buttonOption.isVisible = true
                root.setOnClickListener {
                    optionClick.onClick(posts[adapterPosition])
                }
            }
        }

        override fun bind(item: Post) {
            val data = item as Post
            binding.apply {
                buttonOption.isVisible = false
                imageFavorite.isVisible = false
                imageRoom.loadImageByUrl(data.images?.first(), root.context)
                textTitle.text = data.title
                textType.text = data.postType?.toUpperCase()
                textAddress.text = data.address.toString()
                imageVerify.isVisible = !(item.verify == null || item.verify == false)
                textPrice.text = data.price.toString()+"VND"
            }
        }

    }

    inner class PostSearchViewHolder(val binding : ItemSearchRoomBinding) : BaseViewHolder<Post>(binding){

        init {
            binding.apply {
                root.setOnClickListener { optionClick.onClick(posts[adapterPosition]) }
                buttonOption.isVisible = true
            }
        }

        override fun bind(item: Post) {
            binding.apply {
                buttonOption.isVisible = false
                imageAvatar.loadImageByUrl(user?.avatar, root.context)
                textName.text = user?.name
                textAddress.text = item.address.district + ", "+ item.address.city
                textPrice.text = "${item.minPrice} VND - ${item.maxPrice} VND"
            }
        }

    }

    companion object{
        const val POST_REND = 1
        const val POST_SEARCH = 2
    }
}
