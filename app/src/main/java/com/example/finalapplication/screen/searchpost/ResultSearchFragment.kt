package com.example.finalapplication.screen.searchpost

import android.content.Intent
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.databinding.FragmentListPostBinding
import com.example.finalapplication.screen.postdetail.PostDetailActivity
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.ScrollListenner
import com.example.finalapplication.utils.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ResultSearchFragment :
    BaseFragment<FragmentListPostBinding>(FragmentListPostBinding::inflate),
    ItemRecyclerViewListenner<Post> {

    private val viewModel by sharedViewModel<SearchPostViewModel>()
    private val adapter = ResultPostAdapter(this)
    private var endpage = false
    private var loading = false

    override fun bindData() {
        viewModel.apply {
            posts.observe(this@ResultSearchFragment) { data ->
                if (data.isEmpty()) binding.containerNoData.isVisible = true
                else {
                    binding.containerNoData.isVisible = false
                    adapter.submitList(data)
                    adapter.notifyDataSetChanged()
                }
            }
            isLoading.observe(this@ResultSearchFragment) { data ->
                loading = data
            }
            isPageEnd.observe(this@ResultSearchFragment) { data ->
                endpage = data
                adapter.haveNextPage = !data
            }
        }
    }

    override fun handleEvent() {

        binding.apply {
            recyclerListPost.addOnScrollListener(object :
                ScrollListenner(recyclerListPost.layoutManager as LinearLayoutManager) {
                override fun loadMore() {
                    viewModel.getResultSearch(viewModel.query)
                }

                override fun isLoading() = loading

                override fun isEndPage() = endpage

            })
        }

    }

    override fun initData() {
        binding.apply {
            containerNoData.isVisible = false
            recyclerListPost.adapter = adapter
        }
    }

    override fun onItemClick(item: Post) {
        val intent = Intent(context, PostDetailActivity::class.java)
        intent.putExtra(Constant.INTENT_POST, item)
        intent.putExtra(Constant.INTENT_TYPE_USER,1)
        startActivity(intent)
    }
}
