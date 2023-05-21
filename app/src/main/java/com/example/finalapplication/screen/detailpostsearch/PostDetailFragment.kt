package com.example.finalapplication.screen.detailpostsearch

import android.content.Intent
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.User
import com.example.finalapplication.databinding.FragmentPostSearchDetailBinding
import com.example.finalapplication.screen.chatroom.ChatRoomActivity
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.base.BaseFragment
import com.example.finalapplication.utils.loadImageByUrl
import com.example.finalapplication.utils.showMessage

class PostDetailFragment(val data : Pair<Post, User>) : BaseFragment<FragmentPostSearchDetailBinding>(FragmentPostSearchDetailBinding::inflate) {

    override fun bindData() {

    }

    override fun handleEvent() {
        binding.buttonChat.setOnClickListener {
            val intent = Intent(context, ChatRoomActivity::class.java)
            intent.putExtra(Constant.RECIVER, data.second.id)
            startActivity(intent)
        }
    }

    override fun initData() {
        binding.apply {
            imageAvatar.loadImageByUrl(data.second.avatar, context)
            textName.text = data.second.name
            textPrice.text = "${data.first.minPrice} VND - ${data.first.maxPrice} VND"
            textAddress.text = "${data.first.address.district}, ${data.first.address.city}"
            textDescription.text = data.first.description
        }
    }
}