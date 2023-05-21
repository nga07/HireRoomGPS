package com.example.finalapplication.screen.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.User
import com.example.finalapplication.data.repository.PostRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.base.BaseViewModel

class HomeViewModel(val postRepository: PostRepository) : BaseViewModel() {

    private val _newPosts = MutableLiveData<List<Post>>()
    val newPosts: LiveData<List<Post>>
        get() = _newPosts
    private val _searchPosts = MutableLiveData<List<Pair<Post, User>>>()
    val searchPost: LiveData<List<Pair<Post, User>>>
        get() = _searchPosts
    private val _roomatePosts = MutableLiveData<List<Post>>()
    val roomatePosts : LiveData<List<Post>>
    get() = _roomatePosts

    init {
        getNewPost()
        getPostSearch()
        getPostRoomate()
    }

    fun getNewPost() {
        launchTask<Any>(onRequest = {
            postRepository.getNewPost(object : Listenner<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _newPosts.postValue(data)
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        })
    }

    fun getPostSearch() {
        launchTask<Any>(onRequest = {
            postRepository.getPostSearch(
                Long.MAX_VALUE,
                object : Listenner<List<Pair<Post, User>>> {
                    override fun onSuccess(data: List<Pair<Post, User>>) {
                        _searchPosts.postValue(data)
                    }

                    override fun onError(msg: String) {
                        message.value = msg
                    }

                })
        })
    }

    fun getPostRoomate(){
        launchTask<Any>(onRequest = {
            postRepository.getPostRoomate(object : Listenner<List<Post>>{
                override fun onSuccess(data: List<Post>) {
                    _roomatePosts.postValue(data)
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        })
    }
}