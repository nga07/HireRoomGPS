package com.example.finalapplication.screen.statisticstaff

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.repository.StatisticRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.base.BaseViewModel

class StatisticStaffViewModel(
    val statisticRepository: StatisticRepository
) : BaseViewModel() {

    private val _roomAllNotHired = MutableLiveData<List<Post>>()
    val roomAllNotHired: LiveData<List<Post>>
        get() = _roomAllNotHired
    private val _roomAllNotHired2 = MutableLiveData<List<Post>>()
    val roomAllNotHired2: LiveData<List<Post>>
        get() = _roomAllNotHired2
    private val _roomAllHired = MutableLiveData<List<Post>>()
    val roomAllHired: LiveData<List<Post>>
        get() = _roomAllHired
    private val _roomVerifiedHired = MutableLiveData<List<Post>>()
    val roomVerifiedHired: LiveData<List<Post>>
        get() = _roomVerifiedHired
    private val _roomVerifiedNotHired = MutableLiveData<List<Post>>()
    val roomVerifiedNotHired: LiveData<List<Post>>
        get() = _roomVerifiedNotHired
    private val _roomVerifiedNotHired2 = MutableLiveData<List<Post>>()
    val roomVerifiedNotHired2: LiveData<List<Post>>
        get() = _roomVerifiedNotHired2

    fun statistic(start: Long, end: Long) {
        launchTask<Any>(onRequest = {
            statisticRepository.getAllHired(start, end, object : Listenner<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _roomAllHired.postValue(data)
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        }, isShowLoading = false)
        launchTask<Any>(onRequest = {
            statisticRepository.getAllNotHired(start, end, object : Listenner<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _roomAllNotHired.postValue(data)
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        }, isShowLoading = false)
        launchTask<Any>(onRequest = {
            statisticRepository.getAllNotHired2(start, end, object : Listenner<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _roomAllNotHired2.postValue(data)
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        }, isShowLoading = false)
        launchTask<Any>(onRequest = {
            statisticRepository.getAllVerifyHire(start, end, object : Listenner<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _roomVerifiedHired.postValue(data)
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        }, isShowLoading = false)
        launchTask<Any>(onRequest = {
            statisticRepository.getAllVerifyNotHire(start, end, object : Listenner<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _roomVerifiedNotHired.postValue(data)
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        }, isShowLoading = false)
        launchTask<Any>(onRequest = {
            statisticRepository.getAllVerifyNotHire2(start, end, object : Listenner<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _roomVerifiedNotHired2.postValue(data)
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        }, isShowLoading = false)
    }

}