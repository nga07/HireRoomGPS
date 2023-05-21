package com.example.finalapplication.data.repository

import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.repository.resource.Listenner

interface StatisticRepository {
    fun getHired(start: Long, end: Long, listener: Listenner<List<Post>>)
    fun getNotHired(start: Long, end: Long, listener: Listenner<List<Post>>)
    fun getNotHired2(start: Long, end: Long, listener: Listenner<List<Post>>)
    fun getVerifyHire(start: Long, end: Long, listener: Listenner<List<Post>>)
    fun getVerifyNotHire(start: Long, end: Long, listener: Listenner<List<Post>>)
    fun getVerifyNotHire2(start: Long, end: Long, listener: Listenner<List<Post>>)
    fun getAllHired(start: Long, end: Long, listener: Listenner<List<Post>>)
    fun getAllNotHired(start: Long, end: Long, listener: Listenner<List<Post>>)
    fun getAllNotHired2(start: Long, end: Long, listener: Listenner<List<Post>>)
    fun getAllVerifyHire(start: Long, end: Long, listener: Listenner<List<Post>>)
    fun getAllVerifyNotHire(start: Long, end: Long, listener: Listenner<List<Post>>)
    fun getAllVerifyNotHire2(start: Long, end: Long, listener: Listenner<List<Post>>)
}