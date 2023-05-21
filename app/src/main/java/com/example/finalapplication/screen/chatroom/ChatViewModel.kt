package com.example.finalapplication.screen.chatroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.finalapplication.data.model.Contact
import com.example.finalapplication.data.model.Message
import com.example.finalapplication.data.model.User
import com.example.finalapplication.data.repository.ContactRepository
import com.example.finalapplication.data.repository.MessageRepository
import com.example.finalapplication.data.repository.UserRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.base.BaseViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val contactRepository: ContactRepository
) : BaseViewModel() {
    private val _receiver = MutableLiveData<User>()
    val receiver: LiveData<User>
        get() = _receiver
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User>
        get() = _currentUser
    private val _newMessage = MutableLiveData<Message>()
    val newMessage: LiveData<Message>
        get() = _newMessage
    private val _historyMessage = MutableLiveData<List<Message>>()
    val historyMessage: LiveData<List<Message>>
        get() = _historyMessage

    init {
        launchTask<User>(onRequest = {
            userRepository.getUser("", object : Listenner<User> {
                override fun onSuccess(data: User) {
                    _currentUser.value = data
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        }, isShowLoading = false)
    }

    fun getReciver(id: String?) {
        launchTask<User>(onRequest = {
            userRepository.getUser(id, object : Listenner<User> {
                override fun onSuccess(data: User) {
                    _receiver.value = data
                }

                override fun onError(msg: String) {
                    message.value = msg
                }

            })
        }, isShowLoading = false)
    }

    fun sendMessage(currentUser: User, adversaryUser: User, messages: Message) {
        launchTask<Message>(onRequest = {
            messageRepository.sendMessage(
                currentUser,
                adversaryUser,
                messages,
                object : Listenner<Boolean> {
                    override fun onSuccess(data: Boolean) {
                        // TODO no-ip
                    }

                    override fun onError(msg: String) {
                        message.value = Constant.ERROR_
                    }
                })
        }, isShowLoading = true)
    }

    fun getNewMessage(id: String?) {
        launchTask<Contact>(onRequest = {
            contactRepository.getContact(id, object : Listenner<Contact> {
                override fun onSuccess(data: Contact) {
                    _newMessage.value = data.message
                }

                override fun onError(msg: String) {
                    message.value = Constant.ERROR_
                }
            })
        }, isShowLoading = false)
    }

    fun getHistoryMessage(reciverId: String, lastIndex: Long) {
        launchTask<List<Message>>(onRequest = {
            messageRepository.getHistoryMessage(
                reciverId,
                lastIndex,
                object : Listenner<List<Message>> {
                    override fun onSuccess(data: List<Message>) {
                        _historyMessage.value = data
                        hideLoading(true)
                    }

                    override fun onError(msg: String) {
                        message.value = (Constant.ERROR_)
                        hideLoading(true)
                    }
                })
        })
    }

    fun updateSeenMessage(id: String?) {
        viewModelScope.launch {
            withContext(NonCancellable) {
                contactRepository.updateSeenMessage(id)
            }
        }
    }
}
