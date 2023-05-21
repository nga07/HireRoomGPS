package com.example.finalapplication.screen.requireverify

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.databinding.FragmentListPostBinding
import com.example.finalapplication.screen.mainstaff.MainStaffViewModel
import com.example.finalapplication.screen.postdetail.PostDetailActivity
import com.example.finalapplication.screen.postdetail.PostDetailViewModel
import com.example.finalapplication.screen.searchpost.ResultPostAdapter
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.ScrollListenner
import com.example.finalapplication.utils.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class VerifyingFragment : BaseFragment<FragmentListPostBinding>(FragmentListPostBinding::inflate), ItemRecyclerViewListenner<Post> {

    private val viewModel by sharedViewModel<MainStaffViewModel>()
    private val viewModelVerifying by viewModel<VerifyingViewModel>()
    private val adapter = ResultPostAdapter(this)
    private var endpage = false
    private var loading = false
    private var postsVerifying = mutableListOf<Post>()
    private var postClick: Post? = null
    private val searchPost = mutableListOf<Post>()


    override fun bindData() {
        viewModelVerifying.postVerifying.observe(this){ data ->
            postsVerifying.addAll(data)
            if (data.isEmpty() && postsVerifying.isEmpty()) {
                binding.containerNoData.isVisible = true
                binding.textNoData.text ="Không có yêu cầu nào đang xử lí"
            }
            else {
                binding.containerNoData.isVisible = false
                adapter.submitList(postsVerifying)
                adapter.notifyDataSetChanged()
                if(data.size<10) {
                    endpage= true
                    adapter.haveNextPage = false
                }
            }
        }
        viewModel.isLoading.observe(this) { data ->
            loading = data
        }
        viewModel.currentPost.observe(this) { data ->
            if (data.verify!=null) {
                Log.v("aaaaaa", "have  update")
                postsVerifying.remove(postClick)
                adapter.submitList(postsVerifying)
                adapter.notifyDataSetChanged()
                if(postsVerifying.isEmpty()){
                    binding.containerNoData.isVisible = true
                    binding.textNoData.text = "Không có yêu cầu nào cần xử lí"
                }
            }
        }
    }

    override fun handleEvent() {
        binding.apply {
            recyclerListPost.addOnScrollListener(object :
                ScrollListenner(recyclerListPost.layoutManager as LinearLayoutManager) {
                override fun loadMore() {
                    viewModelVerifying.getPostVerifying(postsVerifying.last().requestTime)
                }

                override fun isLoading() = loading

                override fun isEndPage() = endpage

            })
            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?) = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    findPost(newText)
                    return true
                }

            })
        }
    }

    private fun findPost(query: String?) {
        searchPost.clear()
        for(post in postsVerifying){
            if(post.title.toString().contains(query.toString()) || post.address.toString().contains(query.toString()))
                searchPost.add(post)
        }
        adapter.submitList(searchPost)
        adapter.notifyDataSetChanged()
    }

    override fun initData() {
        adapter.submitList(mutableListOf())
        adapter.notifyDataSetChanged()
        binding.apply {
            containerNoData.isVisible = false
            recyclerListPost.adapter = adapter
            textTitle.isVisible = true
            textTitle.text = "Danh sách đang được xác thực"
            search.isVisible = true
        }
    }

    override fun onItemClick(item: Post) {
        postClick = item
        val intent = Intent(context, PostDetailActivity::class.java)
        intent.putExtra(Constant.INTENT_POST, item)
        intent.putExtra(Constant.INTENT_TYPE_USER,3)
        startActivity(intent)
    }

    override fun onResume() {
        if(postClick != null){
            viewModel.getPost(postClick!!.id.toString())
        }
        super.onResume()
    }
}
