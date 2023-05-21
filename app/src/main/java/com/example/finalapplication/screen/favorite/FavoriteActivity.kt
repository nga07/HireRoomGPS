package com.example.finalapplication.screen.favorite

import android.content.Intent
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.databinding.FragmentFavoriteBinding
import com.example.finalapplication.screen.postdetail.PostDetailActivity
import com.example.finalapplication.screen.postdetail.PostDetailViewModel
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.FavoriteListener
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteActivity : BaseActivity<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate),
    ItemRecyclerViewListenner<Any>, FavoriteListener {

    private val viewModel by viewModel<PostDetailViewModel>()
    private val adapter = PostFavoriteAdapter(this, this)

    override fun bindData() {
        viewModel.postFavorite.observe(this) { data ->
            adapter.setData(data)
        }
    }

    override fun handleEvent() {
        // TODO("Not yet implemented")
    }

    override fun initData() {
        binding.apply {
            recyclerFavorite.adapter = adapter
        }
    }

    override fun onclick(post: Post) {
        viewModel.unFavoritePost(post)
    }

    override fun onItemClick(item: Any) {
        val intent = Intent(this, PostDetailActivity::class.java)
        intent.putExtra(Constant.INTENT_POST, item as Post)
        intent.putExtra(Constant.INTENT_TYPE_USER, 1)
        startActivity(intent)
    }
}
