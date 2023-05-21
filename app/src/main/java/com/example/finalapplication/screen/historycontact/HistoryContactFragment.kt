package com.example.finalapplication.screen.historycontact

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalapplication.data.model.Contact
import com.example.finalapplication.databinding.FragmentHistoryContactBinding
import com.example.finalapplication.screen.chatroom.ChatRoomActivity
import com.example.finalapplication.screen.search.SearchActivity
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.NetworkUtils
import com.example.finalapplication.utils.NumberConstant
import com.example.finalapplication.utils.ScrollListenner
import com.example.finalapplication.utils.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryContactFragment :
    BaseFragment<FragmentHistoryContactBinding>(FragmentHistoryContactBinding::inflate),
    ItemRecyclerViewListenner<Contact> {

    private val historyContactViewModel: HistoryContactViewModel by viewModel()
    private val contactsAdapter = HistoryContactListAdapter(this)
    private var isEndPage = false
    private var isLoading = false
    private val contacts = mutableListOf<Contact>()
    private var isCanLoadMore = false

    override fun handleEvent() {
        binding.apply {
            containerSearch.setOnClickListener {
                val intent = Intent(context, SearchActivity::class.java)
                startActivity(intent)
            }
            recycleHistoryContact.addOnScrollListener(object :
                ScrollListenner(recycleHistoryContact.layoutManager as LinearLayoutManager) {
                override fun loadMore() {
                    contactsAdapter.currentList.last().message?.time?.let {
                        isCanLoadMore = true
                        historyContactViewModel.getListContact(
                            it
                        )
                    }
                }

                override fun isLoading() = isLoading

                override fun isEndPage() = isEndPage
            })
        }
    }

    override fun initData() {
        binding.recycleHistoryContact.adapter = contactsAdapter
        contactsAdapter.submitList(contacts)
        contactsAdapter.notifyDataSetChanged()
    }

    override fun bindData() {
        historyContactViewModel.contacts.observe(this) { data ->
            if (!isCanLoadMore) contacts.clear()
            contacts.addAll(data)
            isCanLoadMore = false
            if (data.size < NumberConstant.ITEM_PER_PAGE) {
                isEndPage = true
                contactsAdapter.haveNextPage = false
            } else {
                isEndPage = false
                contactsAdapter.haveNextPage = true
            }

            binding.apply {
                progressLoading.isVisible = false
                contactsAdapter.submitList(contacts)
                contactsAdapter.notifyDataSetChanged()
            }
        }
        val dialogNoInternet =
            NetworkUtils.getDialogNoInternet(context, layoutInflater, binding.root)
        val isConnected =
            NetworkUtils(context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        isConnected.observe(this) { data ->
            if (data == false) dialogNoInternet.show()
            else dialogNoInternet.hide()
        }
        historyContactViewModel.isLoading.observe(this) { data ->
            this.isLoading = data
        }
    }

    override fun onItemClick(item: Contact) {
        val intent = Intent(context, ChatRoomActivity::class.java)
        intent.putExtra(Constant.RECIVER, item.adversaryUser?.id)
        startActivity(intent)
    }
}
