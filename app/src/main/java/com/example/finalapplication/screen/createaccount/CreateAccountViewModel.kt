package com.example.finalapplication.screen.createaccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.User
import com.example.finalapplication.data.repository.UserRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.base.BaseViewModel

class CreateAccountViewModel(val userRepository: UserRepository) : BaseViewModel() {

    private val _isRegisterSuccess = MutableLiveData<Boolean>()
    val isRegisterSuccess: LiveData<Boolean>
        get() = _isRegisterSuccess

    fun createAccount(user: User) {
        launchTask<Boolean>(onRequest = {
            userRepository.registerUser(user, object : Listenner<Boolean> {

                override fun onError(msg: String) {
                    message.value = Constant.ERROR_ACCOUNT_EXIST
                    hideLoading(true)
                }

                override fun onSuccess(result: Boolean) {
                    _isRegisterSuccess.value = result
                    if (result == false) message.value = Constant.ERROR_
                    hideLoading(true)
                }
            })
        })
    }
}
