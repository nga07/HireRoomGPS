package com.example.finalapplication.screen.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.User
import com.example.finalapplication.data.repository.UserRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.base.BaseViewModel

class SearchViewModel(private val userRepository: UserRepository) : BaseViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    fun getUsersResult(name: String, lastIndex: String) {
        launchTask<List<User>>(
            onRequest = {
                userRepository.getUserByName(name, lastIndex, object : Listenner<List<User>> {
                    override fun onSuccess(data: List<User>) {
                        _users.value = data
                        hideLoading(true)
                    }

                    override fun onError(msg: String) {
                        message.value = msg
                        hideLoading(true)
                    }
                })
            }
        )
    }
}
