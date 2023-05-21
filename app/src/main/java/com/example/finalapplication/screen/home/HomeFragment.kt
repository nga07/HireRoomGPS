package com.example.finalapplication.screen.home

import android.content.Intent
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finalapplication.R
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.User
import com.example.finalapplication.databinding.FragmentHomeBinding
import com.example.finalapplication.screen.createpost.CreatePostActivity
import com.example.finalapplication.screen.createpostsearch.CreatePostSearchActivity
import com.example.finalapplication.screen.detailpostsearch.PostSearchDetailActivity
import com.example.finalapplication.screen.postdetail.PostDetailActivity
import com.example.finalapplication.screen.searchpost.SearchPostActivity
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.ItemRecyclerViewListenner
import com.example.finalapplication.utils.base.BaseFragment
import com.example.finalapplication.utils.showMessage
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    ItemRecyclerViewListenner<Any> {

    private val homeViewModel by viewModel<HomeViewModel>()
    private val trendingAdapter = TrendingAdapter(this)
    private val newRoomAdapter = NewRoomAdapter(this)
    private val newPosts = mutableListOf<Post>()
    private val searchRoomAdapter = SearchRoomAdapter(this)
    private val roomateAdapter = RoomateAdapter(this)

    override fun bindData() {

        homeViewModel.newPosts.observe(this) { data ->
            newPosts.clear()
            newPosts.addAll(data)
            newRoomAdapter.setData(newPosts)
        }
        homeViewModel.searchPost.observe(this) { data ->
            searchRoomAdapter.setData(data)
            searchRoomAdapter.haveNextPage = false
        }
        homeViewModel.roomatePosts.observe(this) { data ->
            roomateAdapter.setData(data)
        }
    }

    override fun handleEvent() {
        binding.apply {
            fabAdd.setOnClickListener {
                if (extendedFabRoom.isVisible) {
                    extendedFabRoom.isVisible = false
                    extendedFabSearchRoom.isVisible = false
                    fabAdd.setImageResource(R.drawable.ic_baseline_add_24)

                } else {
                    extendedFabRoom.isVisible = true
                    extendedFabSearchRoom.isVisible = true
                    fabAdd.setImageResource(R.drawable.ic_baseline_clear_24)
                }
            }
            extendedFabSearchRoom.setOnClickListener {
                extendedFabRoom.isVisible = false
                extendedFabSearchRoom.isVisible = false
                fabAdd.setImageResource(R.drawable.ic_baseline_add_24)
                val intent = Intent(activity, CreatePostSearchActivity::class.java)
                startActivity(intent)
            }
            extendedFabRoom.setOnClickListener {
                extendedFabRoom.isVisible = false
                extendedFabSearchRoom.isVisible = false
                fabAdd.setImageResource(R.drawable.ic_baseline_add_24)
                val intent = Intent(activity, CreatePostActivity::class.java)
                startActivity(intent)
            }
            textMore.setOnClickListener {
                val intent = Intent(context, PostSearchDetailActivity::class.java)
                startActivity(intent)
            }
            containerSearch.setOnClickListener {
                val intent = Intent(context, SearchPostActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun initData() {
        val listTrending = mutableListOf<Pair<String, Int>>()
        listTrending.add(Pair("Quận Nam Từ Liêm", R.drawable.image_bac_tu_niem))
        listTrending.add(Pair("Quận Bắc Từ Liêm", R.drawable.image_nam_tu_niem))
        listTrending.add(Pair("Quận Hoàng Mai", R.drawable.image_hoang_mai))
        listTrending.add(Pair("Quận Hai Bà Trưng", R.drawable.image_hai_ba_trung))
        listTrending.add(Pair("Quận Đống Đa", R.drawable.image_dong_da))
        listTrending.add(Pair("Quận Cầu Giấy", R.drawable.image_cau_giay))
        trendingAdapter.setData(listTrending)
        binding.apply {
            recyclerTrending.adapter = trendingAdapter
            recyclerTrending.layoutManager = GridLayoutManager(context, 3)
            recyclerNewRoom.adapter = newRoomAdapter
            recyclerSearchRoom.adapter = searchRoomAdapter
            recyclerRoomMate.adapter = roomateAdapter
            recyclerRoomMate.layoutManager = GridLayoutManager(context, 2)
        }
    }

    override fun onItemClick(item: Any) {
        when (item) {
            is String -> {
                val intent = Intent(context, SearchPostActivity::class.java)
                intent.putExtra(Constant.INTENT_QUERY, "$item, Hà Nội")
                startActivity(intent)
            }
            is Post -> {
                val intent = Intent(context, PostDetailActivity::class.java)
                intent.putExtra(Constant.INTENT_POST, item)
                intent.putExtra(Constant.INTENT_TYPE_USER, 1)
                startActivity(intent)
            }
            else -> {
                val data = item as Pair<Post, User>
                val intent = Intent(context, PostSearchDetailActivity::class.java)
                intent.putExtra(Constant.INTENT_POST, data.first)
                intent.putExtra(Constant.INTENT_USER, data.second)
                startActivity(intent)
            }
        }
    }

}
