package com.example.finalapplication.data.repository

import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.data.repository.resource.PostDataSource
import com.example.finalapplication.data.repository.resource.StatisticDataSource

class StatisticRepositoryImpl(private val remote: StatisticDataSource) : StatisticRepository {
    override fun getHired(start: Long, end: Long, listener: Listenner<List<Post>>) {
        remote.getHired(start, end, listener)
    }


    override fun getNotHired(start: Long, end: Long, listener: Listenner<List<Post>>) {
        remote.getNotHired(start, end, listener)
    }

    override fun getNotHired2(start: Long, end: Long, listener: Listenner<List<Post>>) {
        remote.getNotHired2(start, end, listener)
    }

    override fun getVerifyHire(start: Long, end: Long, listener: Listenner<List<Post>>) {
        remote.getVerifyHire(start, end, listener)
    }

    override fun getVerifyNotHire(start: Long, end: Long, listener: Listenner<List<Post>>) {
        remote.getVerifyNotHire(start, end, listener)
    }

    override fun getVerifyNotHire2(start: Long, end: Long, listener: Listenner<List<Post>>) {
        remote.getVerifyNotHire2(start, end, listener)
    }


    override fun getAllHired(start: Long, end: Long, listener: Listenner<List<Post>>) {
        remote.getAllHired(start, end, listener)
    }

    override fun getAllNotHired(start: Long, end: Long, listener: Listenner<List<Post>>) {
        remote.getAllNotHired(start, end, listener)
    }

    override fun getAllNotHired2(start: Long, end: Long, listener: Listenner<List<Post>>) {
        remote.getAllNotHired2(start, end, listener)
    }

    override fun getAllVerifyHire(start: Long, end: Long, listener: Listenner<List<Post>>) {
        remote.getAllVerifyHire(start, end, listener)
    }

    override fun getAllVerifyNotHire(start: Long, end: Long, listener: Listenner<List<Post>>) {
        remote.getAllVerifyNotHire(start, end, listener)
    }

    override fun getAllVerifyNotHire2(start: Long, end: Long, listener: Listenner<List<Post>>) {
        remote.getAllVerifyNotHire2(start, end, listener)
    }

}