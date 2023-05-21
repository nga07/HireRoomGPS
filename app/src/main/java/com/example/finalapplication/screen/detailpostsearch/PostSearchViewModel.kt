package com.example.finalapplication.screen.detailpostsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.User
import com.example.finalapplication.data.repository.PostRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.base.BaseViewModel

class PostSearchViewModel(val postRepository: PostRepository) : BaseViewModel() {
    private val _postsSearch = MutableLiveData<List<Pair<Post, User>>>()
    val postsSearch: LiveData<List<Pair<Post, User>>>
        get() = _postsSearch
    private val _postDetail = MutableLiveData<Pair<Post, User>>()
    val postDetail: LiveData<Pair<Post, User>>
        get() = _postDetail

    init {
        getPostSearch(Long.MAX_VALUE)
    }

    fun getPostSearch(lastIndex: Long) {
        launchTask<Any>(onRequest = {
            postRepository.getPostSearch(
                lastIndex,
                object : Listenner<List<Pair<Post, User>>> {
                    override fun onSuccess(data: List<Pair<Post, User>>) {
                        _postsSearch.postValue(data)
                        hideLoading(true)
                    }

                    override fun onError(msg: String) {
                        message.value = msg
                        hideLoading(true)
                    }

                })
        })
    }

    fun setData(data: Pair<Post, User>) {
        _postDetail.postValue(data)
    }

}