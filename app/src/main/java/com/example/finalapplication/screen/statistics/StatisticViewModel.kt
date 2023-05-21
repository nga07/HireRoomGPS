package com.example.finalapplication.screen.statistics

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.repository.StatisticRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.base.BaseViewModel

class StatisticViewModel(
    val statisticRepository: StatisticRepository
) : BaseViewModel() {

    private val _roomNotHired = MutableLiveData<List<Post>>()
    val roomNotHired: LiveData<List<Post>>
        get() = _roomNotHired
    private val _roomNotHired2 = MutableLiveData<List<Post>>()
    val roomNotHired2: LiveData<List<Post>>
        get() = _roomNotHired2
    private val _roomHired = MutableLiveData<List<Post>>()
    val roomHired: LiveData<List<Post>>
        get() = _roomHired
    private val _roomVerifyNotHired = MutableLiveData<List<Post>>()
    val roomVerifyNotHired: LiveData<List<Post>>
        get() = _roomVerifyNotHired
    private val _roomVerifyNotHired2 = MutableLiveData<List<Post>>()
    val roomVerifyNotHired2: LiveData<List<Post>>
        get() = _roomVerifyNotHired2
    private val _roomVerifyHired = MutableLiveData<List<Post>>()
    val roomVerifyHired: LiveData<List<Post>>
        get() = _roomVerifyHired

    fun statistic(start: Long, end: Long) {
        Log.d("endTEST", "statistic: $end")
        launchTask<Any>(onRequest = {
            statisticRepository.getHired(start, end, object : Listenner<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _roomHired.postValue(data)
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        }, isShowLoading = false)
        launchTask<Any>(onRequest = {
            statisticRepository.getNotHired(start, end, object : Listenner<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _roomNotHired.postValue(data)
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        }, isShowLoading = false)
        launchTask<Any>(onRequest = {
            statisticRepository.getNotHired2(start, end, object : Listenner<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _roomNotHired2.postValue(data)
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        }, isShowLoading = false)
        launchTask<Any>(onRequest = {
            statisticRepository.getVerifyNotHire(start, end, object : Listenner<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _roomVerifyNotHired.postValue(data)
                }

                override fun onError(msg: String) {
                    message.value = msg
                    Log.d("testERR", "onError: " + msg)
                }

            })
        }, isShowLoading = false)
        launchTask<Any>(onRequest = {
            statisticRepository.getVerifyNotHire2(start, end, object : Listenner<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _roomVerifyNotHired2.postValue(data)
                    for (i in 0 until data.size) {
                        Log.d("data[i]", " statistic" + data[i].toString())
                    }
                }

                override fun onError(msg: String) {
                    message.value = msg
                    Log.d("testERR", "onError: " + msg)
                }

            })
        }, isShowLoading = false)
        launchTask<Any>(onRequest = {
            statisticRepository.getVerifyHire(start, end, object : Listenner<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _roomVerifyHired.postValue(data)
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        }, isShowLoading = false)
    }

}