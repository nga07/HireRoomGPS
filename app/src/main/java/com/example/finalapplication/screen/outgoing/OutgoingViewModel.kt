package com.example.finalapplication.screen.outgoing


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.DataNotification
import com.example.finalapplication.data.model.Message
import com.example.finalapplication.data.model.Notification
import com.example.finalapplication.data.model.User
import com.example.finalapplication.data.repository.MessageRepository
import com.example.finalapplication.data.repository.NotificationRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.Constant
import com.example.finalapplication.utils.FcmConstant
import com.example.finalapplication.utils.base.BaseViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class OutgoingViewModel(
    val notificationRepository: NotificationRepository,
    val messageRepository: MessageRepository,
) : BaseViewModel() {

    private val _isFinish = MutableLiveData<Boolean>()
    val isFinish: LiveData<Boolean>
        get() = _isFinish

    fun sendNotification(currentUser: User?, adversaryUser: User?, type: String?) {
        if (adversaryUser?.fcmToken.isNullOrEmpty()) {
            message.value = Constant.ERROR_USER_NOT_CONNECT
            return
        }
        val dataNotification = DataNotification(
            currentUser?.id.toString() + adversaryUser?.id,
            currentUser?.name.toString(),
            currentUser?.avatar.toString(),
            currentUser?.fcmToken.toString(),
            FcmConstant.INVITATION,
            type
        )
        val token = listOf(adversaryUser?.fcmToken)
        val notification = Notification(dataNotification, token)
        notificationRepository.sendNotification(
            notification,
            object : Listenner<Boolean> {
                override fun onSuccess(data: Boolean) {
                    // TODO no-ip
                }

                override fun onError(msg: String) {
                    message.value = msg
                }
            })
    }

    fun sendMessage(currentUser: User, adversaryUser: User, messages: Message) {
        launchTask<Message>(onRequest = {
            withContext(NonCancellable) {
                messageRepository.sendMessage(
                    currentUser,
                    adversaryUser,
                    messages,
                    object : Listenner<Boolean> {
                        override fun onSuccess(data: Boolean) {
                            _isFinish.postValue(true)
                        }

                        override fun onError(msg: String) {
                            message.value = Constant.ERROR_
                        }
                    })
            }
        }, isShowLoading = true)
    }
}