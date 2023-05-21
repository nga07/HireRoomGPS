package com.example.finalapplication.screen.search

import android.content.Intent
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalapplication.data.model.User
import com.example.finalapplication.databinding.ActivitySearchBinding
import com.example.finalapplication.screen.chatroom.ChatRoomActivity
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.NumberConstant
import com.example.finalapplication.utils.ScrollListenner
import com.example.finalapplication.utils.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity<ActivitySearchBinding>(ActivitySearchBinding::inflate),
    ItemRecyclerViewListenner<User> {

    private val searchViewModel: SearchViewModel by viewModel()
    private val users = mutableListOf<User>()
    private var isEndPage = false
    private var isLoading = false
    private var currenquery: String? = null
    private val searchListAdapter = SearchListAdapter(this)

    override fun handleEvent() {
        binding.apply {
            buttonBack.setOnClickListener { finish() }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(querry: String?): Boolean {
                    currenquery = querry
                    isEndPage = false
                    searchListAdapter.haveNextPage = true
                    users.clear()
                    querry?.trim()?.let {
                        searchViewModel.getUsersResult(it, "")
                    }
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    return false
                }
            })
            recyleResultSearch.addOnScrollListener(object :
                ScrollListenner(recyleResultSearch.layoutManager as LinearLayoutManager) {
                override fun loadMore() {
                    currenquery?.let {
                        searchViewModel.getUsersResult(
                            it,
                            users.last().id.toString()
                        )
                    }
                }

                override fun isLoading() = isLoading

                override fun isEndPage() = isEndPage
            })
        }
    }

    override fun initData() {
        binding.apply {
            searchView.requestFocus()
        }
        binding.recyleResultSearch.adapter = searchListAdapter
    }

    override fun onItemClick(item: User) {
        val intent = Intent(this, ChatRoomActivity::class.java)
        intent.putExtra(Constant.RECIVER, item.id)
        startActivity(intent)
        finish()
    }

    override fun bindData() {
        searchViewModel.users.observe(this) { data ->
            if (data.size < NumberConstant.ITEM_PER_PAGE) {
                isEndPage = true
                searchListAdapter.haveNextPage = false
            }
            users.addAll(data)
            searchListAdapter.submitList(users)
            searchListAdapter.notifyDataSetChanged()
        }
        searchViewModel.isLoading.observe(this) { data ->
            isLoading = data
            if (data == true) {
                binding.containerLoading.isVisible = true
                binding.progressLoading.isVisible = true
                binding.textNodata.isVisible = false
            } else {
                if (users.size > 0) binding.containerLoading.isVisible = false
                else {
                    binding.progressLoading.isVisible = false
                    binding.textNodata.isVisible = true
                }
            }
        }
    }
}
