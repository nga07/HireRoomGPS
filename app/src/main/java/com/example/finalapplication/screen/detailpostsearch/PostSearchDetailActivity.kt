package com.example.finalapplication.screen.detailpostsearch

import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.User
import com.example.finalapplication.databinding.ActivityPostSearchDetailBinding
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostSearchDetailActivity :
    BaseActivity<ActivityPostSearchDetailBinding>(ActivityPostSearchDetailBinding::inflate) {

    private val postSearchViewModel by viewModel<PostSearchViewModel>()

    override fun bindData() {
        postSearchViewModel.postDetail.observe(this) { data ->
            if (data != null) {
                supportFragmentManager.beginTransaction()
                    .add(binding.containerPost.id, PostDetailFragment(data))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun handleEvent() {
        binding.apply {
            buttonBack.setOnClickListener {
                finish()
            }
        }
    }

    override fun initData() {
        supportFragmentManager.beginTransaction()
            .add(binding.containerPost.id, PostsSearchFragment())
            .commit()
        val post = intent.getSerializableExtra(Constant.INTENT_POST)
        val user = intent.getSerializableExtra(Constant.INTENT_USER)
        if (post != null && user != null) {
            supportFragmentManager.beginTransaction()
                .add(binding.containerPost.id, PostDetailFragment(Pair(post as Post, user as User)))
                .addToBackStack(null)
                .commit()
        }
    }

}