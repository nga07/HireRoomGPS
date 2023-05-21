package com.example.finalapplication.screen.mypost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.User
import com.example.finalapplication.data.repository.PostRepository
import com.example.finalapplication.data.repository.UserRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.base.BaseViewModel

class MyPostViewModel(
    val postRepository: PostRepository,
    val userRepository: UserRepository
) : BaseViewModel() {

    private val _myPosts = MutableLiveData<List<Post>>()
    val myPosts: LiveData<List<Post>>
        get() = _myPosts
    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    init {
        launchTask<Any>(onRequest = {
            postRepository.getMyPost(object : Listenner<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _myPosts.postValue(data)
                }

                override fun onError(msg: String) {
                    message.value = msg
                }
            })
        }, isShowLoading = false)
        launchTask<Any>(onRequest = {
            userRepository.getUser("", object : Listenner<User> {
                override fun onSuccess(data: User) {
                    _user.postValue(data)
                }

                override fun onError(msg: String) {
                    message.postValue(msg)
                }

            })
        })
    }

    fun requestVerify(post : Post){
        postRepository.requestVerifiy(post, object  : Listenner<Boolean>{
            override fun onSuccess(data: Boolean) {
                if(data) message.postValue("Gửi yêu cầu thành công")
            }

            override fun onError(msg: String) {
                message.postValue(msg)
            }

        })
    }
    fun changeActivityPost(post: Post){
        postRepository.changeAcitivyPost(post, object : Listenner<Boolean>{
            override fun onSuccess(data: Boolean) {
                if (data) message.postValue("Đã cập nhật")
            }

            override fun onError(msg: String) {
                message.postValue(msg.toString())
            }

        })
    }
    fun deletePost(post: Post){
        postRepository.deletePost(post, object : Listenner<Boolean>{
            override fun onSuccess(data: Boolean) {
                if (data) message.postValue("Đã xoá")
            }

            override fun onError(msg: String) {
                message.postValue(msg.toString())
            }

        })
    }
}