package com.example.finalapplication.screen.launch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.User
import com.example.finalapplication.data.repository.UserRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.base.BaseViewModel

class LaunchViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    init {
        launchTask<User>(
            onRequest = {
                userRepository.getUser(null, object : Listenner<User> {
                    override fun onSuccess(data: User) {
                        _user.value = data
                        hideLoading(true)
                    }

                    override fun onError(msg: String) {
                        message.value = msg
                        _user.value = null
                        hideLoading(true)
                    }
                })
            },
        )
    }
}
