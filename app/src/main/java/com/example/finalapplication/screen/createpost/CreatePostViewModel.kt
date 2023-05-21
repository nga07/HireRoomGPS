package com.example.finalapplication.screen.createpost

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.repository.PostRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.base.BaseViewModel

class CreatePostViewModel(private val postRepository: PostRepository) : BaseViewModel() {
    private val _isFirstState = MutableLiveData<Int>(0)
    val isFirstState: LiveData<Int>
        get() = _isFirstState
    private var post = Post()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean>
        get() = _isSuccess

    fun setState(state: Int) {
        _isFirstState.postValue(state)
    }

    fun setPost(post: Post) {
        this.post = post
    }

    fun getPost() = post

    fun createPost(post: Post) {
        launchTask<Boolean>(onRequest = {
            postRepository.addPost(post, object : Listenner<Boolean> {
                override fun onSuccess(data: Boolean) {
                    _isSuccess.postValue(true)
                    hideLoading(true)
                }

                override fun onError(msg: String) {
                    Log.v("aaaaa", msg)
                    hideLoading(true)
                }
            })
        })
    }

    fun upDatePost(post: Post) {
        launchTask<Any>(onRequest = {
            postRepository.updatePost(post, object : Listenner<Boolean> {
                override fun onSuccess(data: Boolean) {
                    _isSuccess.postValue(true)
                    hideLoading(true)
                }

                override fun onError(msg: String) {
                    Log.v("aaaaa", msg)
                    hideLoading(true)
                }
            })
        })
    }
}
