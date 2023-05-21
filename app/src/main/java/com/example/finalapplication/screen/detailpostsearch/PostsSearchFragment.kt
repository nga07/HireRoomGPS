package com.example.finalapplication.screen.detailpostsearch

import android.app.ProgressDialog
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.User
import com.example.finalapplication.databinding.FragmentListPostBinding
import com.example.finalapplication.screen.home.SearchRoomAdapter
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.NumberConstant
import com.example.finalapplication.utils.ScrollListenner
import com.example.finalapplication.utils.base.BaseFragment
import com.example.finalapplication.utils.showMessage
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostsSearchFragment :
    BaseFragment<FragmentListPostBinding>(FragmentListPostBinding::inflate),
    ItemRecyclerViewListenner<Any> {

    private val postSearchViewModel by sharedViewModel<PostSearchViewModel>()
    private val postsSearch = mutableListOf<Pair<Post, User>>()
    private var isEndPage = false
    private var isFirstLoad = true
    private val searchRoomAdapter = SearchRoomAdapter(this)
    private var loading = false

    override fun bindData() {
        postSearchViewModel.postsSearch.observe(this) { data ->
            postsSearch.addAll(data)
            if (postsSearch.isNotEmpty()) {
                binding.containerNoData.isVisible = false
            }
            searchRoomAdapter.setData(postsSearch)
            isFirstLoad = false
            if (data.size < NumberConstant.ITEM_PER_PAGE) {
                isEndPage = true
                searchRoomAdapter.haveNextPage = false
            }
        }
        postSearchViewModel.isLoading.observe(this) { data ->
            loading = data
        }
    }

    override fun handleEvent() {
        binding.apply {
            recyclerListPost.addOnScrollListener(object :
                ScrollListenner(recyclerListPost.layoutManager as LinearLayoutManager) {
                override fun loadMore() {
                    postSearchViewModel.getPostSearch(postsSearch.last().first.time)
                }

                override fun isLoading() = loading

                override fun isEndPage() = isEndPage

            })
        }
    }

    override fun initData() {
        binding.apply {
            recyclerListPost.adapter = searchRoomAdapter
        }
    }

    override fun onItemClick(item: Any) {
        val data = item as Pair<Post, User>
        postSearchViewModel.setData(item as Pair<Post, User>)
    }
}