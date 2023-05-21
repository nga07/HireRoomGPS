package com.example.finalapplication.data.repository.resource.remote

import android.util.Log
import com.example.finalapplication.data.model.HistorySearch
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.data.repository.resource.StatisticDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class RemoteStatistic : StatisticDataSource {
    private val auth = FirebaseAuth.getInstance()
    private val database = Firebase.firestore
    private val storage = Firebase.storage.reference
    override fun getHired(start: Long, end: Long, listener: Listenner<List<Post>>) {

        database.collection(Post.posts)
            .whereEqualTo(Post.uid, auth.uid)
            .whereIn(Post.type, arrayListOf(Post.phongChoThue, Post.nhaChoThue, Post.phongOGhep))
            .whereEqualTo(Post.available, false)
            .whereGreaterThanOrEqualTo(Post.timeHired, start)
            .whereLessThan(Post.timeHired, end)
            .orderBy(Post.timeHired, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val posts = mutableListOf<Post>()
                    for (data in value.documents) {
                        val post = data.toObject(Post::class.java)
                        post?.let { posts.add(it) }
                    }
                    listener.onSuccess(posts)
                }
            }
    }

    override fun getNotHired(start: Long, end: Long, listener: Listenner<List<Post>>) {
        database.collection(Post.posts)
            .whereEqualTo(Post.uid, auth.uid)
            .whereIn(Post.type, arrayListOf(Post.phongChoThue, Post.nhaChoThue, Post.phongOGhep))
            .whereEqualTo(Post.available, true)
            .whereLessThan(Post.time, end)
            .orderBy(Post.time, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val posts = mutableListOf<Post>()
                    for (data in value.documents) {
                        val post = data.toObject(Post::class.java)
                        post?.let { posts.add(it) }
                    }
                    for (i in 0 until posts.size) {
                        Log.d("data[i]", " not hired " + posts[i].toString())
                    }
                    listener.onSuccess(posts)
                }
            }
    }

    override fun getNotHired2(start: Long, end: Long, listener: Listenner<List<Post>>) {
        database.collection(Post.posts)
            .whereEqualTo(Post.uid, auth.uid)
            .whereIn(Post.type, arrayListOf(Post.phongChoThue, Post.nhaChoThue, Post.phongOGhep))
            .whereEqualTo(Post.available, false)
            .whereGreaterThanOrEqualTo(Post.timeHired, end)
            .orderBy(Post.timeHired, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val posts = mutableListOf<Post>()
                    for (data in value.documents) {
                        val post = data.toObject(Post::class.java)
                        post?.let { posts.add(it) }
                    }
                    for (i in 0 until posts.size) {
                        Log.d("data[i]", " not hired 2 " + posts[i].toString())
                    }
                    listener.onSuccess(posts)
                }
            }
    }


    override fun getVerifyHire(start: Long, end: Long, listener: Listenner<List<Post>>) {
        database.collection(Post.posts)
            .whereEqualTo(Post.uid, auth.uid)
            .whereIn(Post.type, arrayListOf(Post.phongChoThue, Post.nhaChoThue, Post.phongOGhep))
            .whereEqualTo(Post.verified, true)
            .whereEqualTo(Post.available, false)
            .whereGreaterThanOrEqualTo(Post.timeHired, start)
            .whereLessThan(Post.timeHired, end)
            .orderBy(Post.timeHired, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val posts = mutableListOf<Post>()
                    for (data in value.documents) {
                        val post = data.toObject(Post::class.java)
                        post?.let { posts.add(it) }
//                        for (i in 0 until posts.size) {
//                            Log.d("data[i]", " hired " + posts[i].toString())
//                        }
                    }
                    listener.onSuccess(posts)
                }
            }
    }

    override fun getVerifyNotHire(start: Long, end: Long, listener: Listenner<List<Post>>) {

        database.collection(Post.posts)
            .whereEqualTo(Post.uid, auth.uid)
            .whereIn(Post.type, arrayListOf(Post.phongChoThue, Post.nhaChoThue, Post.phongOGhep))
            .whereEqualTo(Post.verified, true)
            .whereEqualTo(Post.available, true)
            .whereLessThan(Post.time, end)
            .orderBy(Post.time, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val posts = mutableListOf<Post>()
                    for (data in value.documents) {
                        val post = data.toObject(Post::class.java)
                        post?.let { posts.add(it) }
                    }
//                    for (i in 0 until posts.size) {
//                        Log.d("data[i]", " " + posts[i].toString())
//                    }
                    listener.onSuccess(posts)
                }
            }
    }


    override fun getVerifyNotHire2(start: Long, end: Long, listener: Listenner<List<Post>>) {

        database.collection(Post.posts)
            .whereEqualTo(Post.uid, auth.uid)
            .whereIn(Post.type, arrayListOf(Post.phongChoThue, Post.nhaChoThue, Post.phongOGhep))
            .whereEqualTo(Post.verified, true)
            .whereEqualTo(Post.available, false)
            .whereGreaterThanOrEqualTo(Post.timeHired, end)
            .orderBy(Post.timeHired, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val posts = mutableListOf<Post>()
                    for (data in value.documents) {
                        val post = data.toObject(Post::class.java)
                        post?.let { posts.add(it) }
                    }
//                    for (i in 0 until posts.size) {
//                        Log.d("data[i]", " " + posts[i].toString())
//                    }
                    listener.onSuccess(posts)
                }
            }
    }


    override fun getAllHired(start: Long, end: Long, listener: Listenner<List<Post>>) {
        database.collection(Post.posts)
            .whereIn(Post.type, arrayListOf(Post.phongChoThue, Post.nhaChoThue, Post.phongOGhep))
            .whereEqualTo(Post.available, false)
            .whereGreaterThanOrEqualTo(Post.timeHired, start)
            .whereLessThan(Post.timeHired, end)
            .orderBy(Post.timeHired, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val posts = mutableListOf<Post>()
                    for (data in value.documents) {
                        val post = data.toObject(Post::class.java)
                        post?.let { posts.add(it) }
                    }
                    listener.onSuccess(posts)
                }
            }
    }

    override fun getAllNotHired(start: Long, end: Long, listener: Listenner<List<Post>>) {
        database.collection(Post.posts)
            .whereIn(Post.type, arrayListOf(Post.phongChoThue, Post.nhaChoThue, Post.phongOGhep))
            .whereEqualTo(Post.available, true)
            .whereLessThan(Post.time, end)
            .orderBy(Post.time, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val posts = mutableListOf<Post>()
                    for (data in value.documents) {
                        val post = data.toObject(Post::class.java)
                        post?.let { posts.add(it) }
                    }
                    listener.onSuccess(posts)
                }
            }
    }

    override fun getAllNotHired2(start: Long, end: Long, listener: Listenner<List<Post>>) {
        database.collection(Post.posts)
            .whereIn(Post.type, arrayListOf(Post.phongChoThue, Post.nhaChoThue, Post.phongOGhep))
            .whereEqualTo(Post.available, false)
            .whereGreaterThanOrEqualTo(Post.timeHired, end)
            .orderBy(Post.timeHired, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val posts = mutableListOf<Post>()
                    for (data in value.documents) {
                        val post = data.toObject(Post::class.java)
                        post?.let { posts.add(it) }
                    }
                    for (i in 0 until posts.size) {
                        Log.d("data[i]", " " + posts[i].toString())
                    }
                    listener.onSuccess(posts)
                }
            }
    }

    override fun getAllVerifyHire(start: Long, end: Long, listener: Listenner<List<Post>>) {
        database.collection(Post.posts)
            .whereIn(Post.type, arrayListOf(Post.phongChoThue, Post.nhaChoThue, Post.phongOGhep))
            .whereEqualTo(Post.verified, true)
            .whereEqualTo(Post.available, false)
            .whereGreaterThanOrEqualTo(Post.timeHired, start)
            .whereLessThan(Post.timeHired, end)
            .orderBy(Post.timeHired, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val posts = mutableListOf<Post>()
                    for (data in value.documents) {
                        val post = data.toObject(Post::class.java)
                        post?.let { posts.add(it) }
                    }
                    listener.onSuccess(posts)
                }
            }

    }

    override fun getAllVerifyNotHire(start: Long, end: Long, listener: Listenner<List<Post>>) {
        database.collection(Post.posts)
            .whereIn(Post.type, arrayListOf(Post.phongChoThue, Post.nhaChoThue, Post.phongOGhep))
            .whereEqualTo(Post.verified, true)
            .whereEqualTo(Post.available, true)
            .whereLessThan(Post.time, end)
            .orderBy(Post.time, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val posts = mutableListOf<Post>()
                    for (data in value.documents) {
                        val post = data.toObject(Post::class.java)
                        post?.let { posts.add(it) }
                    }
                    listener.onSuccess(posts)
                }
            }
    }

    override fun getAllVerifyNotHire2(start: Long, end: Long, listener: Listenner<List<Post>>) {
        database.collection(Post.posts)
            .whereIn(Post.type, arrayListOf(Post.phongChoThue, Post.nhaChoThue, Post.phongOGhep))
            .whereEqualTo(Post.verified, true)
            .whereEqualTo(Post.available, false)
            .whereGreaterThanOrEqualTo(Post.timeHired, end)
            .orderBy(Post.timeHired, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val posts = mutableListOf<Post>()
                    for (data in value.documents) {
                        val post = data.toObject(Post::class.java)
                        post?.let { posts.add(it) }
                    }
                    listener.onSuccess(posts)
                }
            }
    }


}