package com.example.finalapplication.data.repository.resource

interface Listenner<T> {
    fun onSuccess(data: T)
    fun onError(msg: String)
}
