package com.example.finalapplication.screen.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.Account
import com.example.finalapplication.data.model.User
import com.example.finalapplication.data.repository.UserRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.base.BaseViewModel
import kotlin.math.log

class LoginViewModel(private val userRepository: UserRepository) : BaseViewModel() {
    private val _isLoginSucces = MutableLiveData<Boolean>()
    val isLoginSuccess: LiveData<Boolean>
        get() = _isLoginSucces

    private val _resetSuccess = MutableLiveData<Boolean>()
    val isResetSuccess: LiveData<Boolean>
        get() = _resetSuccess

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _userEmtpty = MutableLiveData<Boolean>()
    val userEmpty: LiveData<Boolean>
        get() = _userEmtpty


    init {
        _isLoginSucces.value = false
        _resetSuccess.value = false
    }

    fun login(account: Account) {
        launchTask<Boolean>(onRequest = {
            userRepository.loginUser(account, object : Listenner<Boolean> {

                override fun onError(msg: String) {
                    message.value = msg
                    hideLoading(true)
                }

                override fun onSuccess(result: Boolean) {
                    _isLoginSucces.value = result
                    hideLoading(true)
                }
            })
        })
    }

    fun forgotPassword(email: String) {
        launchTask<Boolean>(onRequest = {
            userRepository.forgotPassword(email, object : Listenner<Boolean> {
                override fun onSuccess(result: Boolean) {
                    _resetSuccess.value = result
                    hideLoading(true)
                }

                override fun onError(msg: String) {
                    message.value = msg
                    hideLoading(true)
                }
            })
        })
    }

    fun getCurrentUser() {
        launchTask<Any>(onRequest = {
            userRepository.getUser("", object : Listenner<User> {
                override fun onSuccess(data: User) {
                    _user.value = data
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        }, isShowLoading = false)
    }

    fun getUserByEmail(email: String) {
        launchTask<Any>(onRequest = {
            userRepository.getUserByEmail(email, object : Listenner<User?> {
                override fun onSuccess(data: User?) {
                    if (data == null) _userEmtpty.postValue(true)
                    else {
                        login(data.userAccount!!)
                    }
                }

                override fun onError(msg: String) {
                    Log.v("aaaaa", msg)
                }

            })
        })
    }
}
