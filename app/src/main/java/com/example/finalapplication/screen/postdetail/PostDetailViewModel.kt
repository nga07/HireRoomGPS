package com.example.finalapplication.screen.postdetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.PostFavorite
import com.example.finalapplication.data.model.User
import com.example.finalapplication.data.repository.PostRepository
import com.example.finalapplication.data.repository.UserRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.base.BaseViewModel

class PostDetailViewModel(val userRepository: UserRepository, val postRepository: PostRepository) : BaseViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user
    private val _postFavorite = MutableLiveData<List<Post>>()
    val postFavorite: LiveData<List<Post>>
        get() = _postFavorite
    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess : LiveData<Boolean>
    get() = _updateSuccess


    init{
        getPostFavorite(Long.MAX_VALUE)
    }

    fun getUser(id: String) {
        launchTask<Any>(onRequest = {
            userRepository.getUser(id, object : Listenner<User> {
                override fun onSuccess(data: User) {
                    _user.postValue(data)
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        },isShowLoading = false)
    }

    fun getPostFavorite(lastIndex : Long){
        launchTask<Any>(onRequest = {
            postRepository.getPostFavorite(lastIndex, object : Listenner<List<PostFavorite>>{
                override fun onSuccess(data: List<PostFavorite>) {
                    val posts = mutableListOf<Post>()
                    for(post in data){
                        post.post?.let { posts.add(it) }
                    }
                    _postFavorite.value = posts
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        }, isShowLoading = false)
    }

    fun addPostFavorite(post : Post){
        launchTask<Any>(onRequest = {
            postRepository.addPostFavorite(post)
        }, isShowLoading = false)
    }

    fun unFavoritePost(post: Post){
        launchTask<Any>(onRequest = {
            postRepository.unFavoritePost(post)
        },isShowLoading = false)
    }

    fun updatePost(post: Post){
        launchTask<Any>(onRequest = {
            postRepository.updatePost(post, object : Listenner<Boolean>{
                override fun onSuccess(data: Boolean) {
                    _updateSuccess.postValue(data)
                }

                override fun onError(msg: String) {
                    message.value = msg
                    Log.v("aaaaa", msg)
                }

            })
        })
    }
}
