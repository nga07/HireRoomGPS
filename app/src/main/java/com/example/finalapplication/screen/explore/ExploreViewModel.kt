package com.example.finalapplication.screen.explore

import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.repository.PostRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.base.BaseViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.util.logging.Handler

class ExploreViewModel(postRepository: PostRepository) : BaseViewModel() {

    private val allPosts = mutableListOf<Post>()

    private val _postVicinty = MutableLiveData<List<Pair<Post, Double>>>()
    val postVicinty: LiveData<List<Pair<Post, Double>>>
        get() = _postVicinty

    init {
        launchTask<Any>(onRequest = {
            postRepository.getAllPost(object : Listenner<List<Post>> {

                override fun onSuccess(data: List<Post>) {
                    allPosts.addAll(data)
                    hideLoading(true)
                }

                override fun onError(msg: String) {
                    Log.v("aaaaa", msg)
                }

            })
        }, isShowLoading = true)
    }

    fun findPostVicinty(location: LatLng) {
        launchTask<Any>(onRequest = {
            val rs = mutableListOf<Pair<Post, Double>>()
            for (post in allPosts) {
                if(post.address.latitude!= null && post.address.longitude != null){
                    val location2 =
                        post.address.longitude?.let {
                            post.address.latitude?.let { it1 ->
                                LatLng(
                                    it1,
                                    it
                                )
                            }
                        }
                    val distance = SphericalUtil.computeDistanceBetween(location, location2)
                    val dis = distance / 1000
                    if (dis < 5) rs.add(Pair(post, dis))
                }
            }
            _postVicinty.postValue(rs)
            hideLoading(true)
        })
    }
}
