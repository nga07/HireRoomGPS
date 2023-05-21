package com.example.finalapplication.data.repository

import com.example.finalapplication.data.model.Notification
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.ApiFcmService
import com.example.finalapplication.utils.FcmConstant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationRepositoryImpl(val apiFcmService: ApiFcmService) : NotificationRepository {

    override fun sendNotification(body: Notification, listen: Listenner<Boolean>) {
        apiFcmService.sendRemoteMessage(FcmConstant.getHeader(), body)
            .enqueue(object : Callback<Notification> {
                override fun onResponse(
                    call: Call<Notification>,
                    response: Response<Notification>
                ) {
                    if (response.isSuccessful) listen.onSuccess(true)
                    else listen.onError(response.message().toString())
                }

                override fun onFailure(call: Call<Notification>, t: Throwable) {
                    listen.onError(t.toString())
                }
            })
    }
}
