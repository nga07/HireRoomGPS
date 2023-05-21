package com.example.finalapplication.screen.requireverify

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.repository.PostRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.base.BaseViewModel

class RequireViewModel(val postRepository: PostRepository) : BaseViewModel() {

    private val _postRequire = MutableLiveData<List<Post>>()
    val postRequire: LiveData<List<Post>>
        get() = _postRequire

    init {
        getPostRequire(0)
    }

    fun getPostRequire(lastIndex : Long){
        launchTask<Any>(onRequest = {
            postRepository.getPostRequire(lastIndex, object : Listenner<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _postRequire.value = (data)
                    Log.v("aaaaa", "have ${data.size} require")
                }

                override fun onError(msg: String) {
                    Log.v("aaaaa", msg)
                }

            })
        })
    }
}
