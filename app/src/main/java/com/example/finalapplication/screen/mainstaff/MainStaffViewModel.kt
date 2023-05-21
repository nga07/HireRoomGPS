package com.example.finalapplication.screen.mainstaff

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.repository.PostRepository
import com.example.finalapplication.data.repository.UserRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.base.BaseViewModel

class MainStaffViewModel(val postRepository: PostRepository, val userRepository: UserRepository): BaseViewModel() {

    private val _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess: LiveData<Boolean>
        get() = _logoutSuccess

    private val _crrentPost = MutableLiveData<Post>()
    val currentPost : LiveData<Post>
    get() =  _crrentPost

    fun logout() {
        userRepository.logout(object : Listenner<Boolean> {
            override fun onSuccess(data: Boolean) {
                _logoutSuccess.value = data
            }

            override fun onError(msg: String) {
                // TODO("Not yet implemented")
            }

        })
    }

    fun getPost(id : String){
        launchTask<Any>(onRequest = {
            postRepository.getPost(id, object : Listenner<Post>{
                override fun onSuccess(data: Post) {
                    _crrentPost.value = data
                }

                override fun onError(msg: String) {
                    Log.v("aaaaa", msg)
                }

            })
        },isShowLoading = false)
    }
}
