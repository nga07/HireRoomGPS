package com.example.finalapplication.screen.requireverify

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.repository.PostRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.base.BaseViewModel

class VerifyingViewModel(val postRepository: PostRepository) : BaseViewModel() {

    private val _postVerifying = MutableLiveData<List<Post>>()
    val postVerifying: LiveData<List<Post>>
        get() = _postVerifying

    init {
        getPostVerifying(0)
    }

    fun getPostVerifying(lastIndex : Long){
        launchTask<Any>(onRequest = {
            postRepository.getPostVerifying(lastIndex, object : Listenner<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _postVerifying.value = (data)
                    Log.v("aaaaa", "have ${data.size} verifying")
                }

                override fun onError(msg: String) {
                    Log.v("aaaaa", msg)
                }

            })
        })
    }

}
