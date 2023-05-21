package com.example.finalapplication.data.repository.resource.remote

import android.location.Geocoder
import android.util.Log
import androidx.core.net.toUri
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.PostFavorite
import com.example.finalapplication.data.model.User
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.data.repository.resource.PostDataSource
import com.example.finalapplication.utils.getNewid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class RemotePost : PostDataSource {

    private val auth = FirebaseAuth.getInstance()
    private val database = Firebase.firestore
    private val storage = Firebase.storage.reference

    override fun addNewPost(post: Post, listener: Listenner<Boolean>) {
        post.uid = auth.uid
        val urlImages = mutableListOf<String>()
        if (post.images != null && post.images?.size!! > 0) {
            for (image in post.images!!) {
                val idImage = getNewid()
                storage.child("image/$idImage")
                    .putFile(image?.toUri()!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            storage.child("image/$idImage")
                                .downloadUrl
                                .addOnSuccessListener { url ->
                                    urlImages.add(url.toString())
                                    if (post.images!!.last().equals(image)) {
                                        post.images = urlImages
                                        addPost(post, listener)
                                    }
                                }
                        } else listener.onError(task.exception.toString())
                    }
            }
        } else addPost(post, listener)
    }

    override fun getNewPost(listener: Listenner<List<Post>>) {
        database.collection(Post.posts)
            .whereIn(Post.type, listOf(Post.nhaChoThue, Post.phongChoThue))
            .orderBy(Post.time, Query.Direction.DESCENDING)
            .whereEqualTo(Post.active, true)
            .limit(6)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val list = mutableListOf<Post>()
                    for (doc in value.documents) {
                        val post = doc.toObject(Post::class.java)
                        if (post != null) {
                            list.add(post)
                        }
                    }
                    listener.onSuccess(list)
                }
            }
    }

    override fun getPostSearchRoom(lastIndex: Long, listener: Listenner<List<Pair<Post, User>>>) {
        database.collection(Post.posts)
            .whereIn(Post.type, listOf(Post.timPhongO))
            .orderBy(Post.time, Query.Direction.DESCENDING)
            .whereLessThan(Post.time, lastIndex)
            .whereEqualTo(Post.active, true)
            .limit(6)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val list = mutableListOf<Pair<Post, User>>()
                    for (doc in value.documents) {
                        val post = doc.toObject(Post::class.java)
                        if (post != null) {
                            database.collection(User.users)
                                .document(post.uid.toString())
                                .addSnapshotListener { v, _ ->
                                    val user = v?.toObject(User::class.java)
                                    user?.let { list.add(Pair(post, it)) }
                                    if (value.documents.last() == doc) {
                                        listener.onSuccess(list)
                                    }
                                }
                        }
                    }
                }
            }
    }

    override fun getPostRoomMate(lastIndex: Long, listener: Listenner<List<Post>>) {
        database.collection(Post.posts)
            .whereIn(Post.type, listOf(Post.phongOGhep))
            .orderBy(Post.time, Query.Direction.DESCENDING)
            .whereLessThan(Post.time, lastIndex)
            .whereEqualTo(Post.active, true)
            .limit(10)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val list = mutableListOf<Post>()
                    for (doc in value.documents) {
                        val post = doc.toObject(Post::class.java)
                        if (post != null) {
                            list.add(post)
                        }
                    }
                    listener.onSuccess(list)
                }
            }
    }

    override fun getPostFavorite(lastIndex: Long, listener: Listenner<List<PostFavorite>>) {
        database.collection(PostFavorite.name)
            .orderBy(PostFavorite.time, Query.Direction.DESCENDING)
            .whereEqualTo("post.${Post.active}", true)
            .whereLessThan(PostFavorite.time, lastIndex)
            .whereEqualTo(Post.uid, auth.uid)
            .addSnapshotListener { data, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    Log.v("aaaaa", error.toString())
                    return@addSnapshotListener
                }
                if (data != null) {
                    val list = mutableListOf<PostFavorite>()
                    for (doc in data.documents) {
                        val post = doc.toObject(PostFavorite::class.java)
                        if (post != null) {
                            list.add(post)
                        }
                    }
                    listener.onSuccess(list)
                }
            }
    }

    override fun addPostFavorite(post: Post) {
        val postFavorite = PostFavorite(
            post.id.toString(),
            auth.uid.toString(),
            System.currentTimeMillis(),
            post
        )
        database.collection(PostFavorite.name)
            .document(postFavorite.id.toString())
            .set(postFavorite)
    }

    override fun unFavoritePost(postFavorite: Post) {
        database.collection(PostFavorite.name)
            .document(postFavorite.id.toString())
            .delete()
    }

    override fun getResultSearch(
        lastIndex: Long,
        ward: String,
        district: String,
        province: String,
        listener: Listenner<List<Post>>
    ) {
        if (ward.isNullOrEmpty() && district.isNullOrEmpty()) {
            getbyProvince(lastIndex, province, listener)
        } else if (ward.isNullOrEmpty()) {
            getbyDistrict(lastIndex, district, listener)
        } else {
            database.collection(Post.posts)
                .whereEqualTo("address.ward", ward.trim())
                .whereEqualTo(Post.active, true)
                .whereEqualTo(Post.available, true)
                .orderBy(Post.time, Query.Direction.DESCENDING)
                .whereLessThan(Post.time, lastIndex)
                .whereIn(Post.type, listOf(Post.phongChoThue, Post.phongOGhep, Post.nhaChoThue))
                .limit(20)
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

    override fun getMypost(listener: Listenner<List<Post>>) {
        Log.d("mypost", "getMypost: " + Post.uid + " " + auth.uid)
        database.collection(Post.posts)
            .whereEqualTo(Post.uid, auth.uid)
            .whereEqualTo(Post.active, true)
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

    override fun requestVerifiy(post: Post, listener: Listenner<Boolean>) {
        post.requestTime = System.currentTimeMillis()
        database.collection(Post.posts)
            .document(post.id.toString())
            .update(mapOf(Post.requestVerify to true))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) listener.onSuccess(true)
                else listener.onError(task.exception.toString())
            }
        database.collection(PostFavorite.name)
            .document(post.id.toString())
            .update(mapOf("post.${Post.requestVerify}" to true))
    }

    override fun changeAcitivyPost(post: Post, listener: Listenner<Boolean>) {
        database.collection(Post.posts)
            .document(post.id.toString())
            .update(mapOf(Post.available to post.available, Post.timeHired to post.timeHired))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) listener.onSuccess(true)
                else listener.onError(task.exception.toString())
            }
        database.collection(PostFavorite.name)
            .document(post.id.toString())
            .update(mapOf("post.${Post.available}" to post.available))

    }

    override fun deletePost(post: Post, listener: Listenner<Boolean>) {
        database.collection(Post.posts)
            .document(post.id.toString())
            .update(mapOf(Post.active to false))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) listener.onSuccess(true)
                else listener.onError(task.exception.toString())
            }
        database.collection(PostFavorite.name)
            .document(post.id.toString())
            .update(mapOf("post.${Post.active}" to false))
    }

    override fun getPostRequire(lastIndex: Long, listener: Listenner<List<Post>>) {
        database.collection(Post.posts)
            .whereEqualTo(Post.requestVerify, true)
            .whereEqualTo(Post.verifying, false)
            .whereEqualTo(Post.verified, null)
            .whereEqualTo(Post.active, true)
            .orderBy(Post.requireTime, Query.Direction.DESCENDING)
            .whereGreaterThan(Post.requireTime, lastIndex)
            .limit(10)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Post>()
                    for (doc in task.result.documents) {
                        val post = doc.toObject(Post::class.java)
                        if (post != null) list.add(post)
                    }
                    listener.onSuccess(list)
                } else {
                    listener.onError(task.exception.toString())
                }
            }
    }

    override fun getPostVerifying(lastIndex: Long, listener: Listenner<List<Post>>) {
        database.collection(Post.posts)
            .whereEqualTo(Post.verifying, true)
            .whereEqualTo(Post.verified, null)
            .whereEqualTo(Post.active, true)
            .orderBy(Post.requireTime, Query.Direction.DESCENDING)
            .whereGreaterThan(Post.requireTime, lastIndex)
            .limit(10)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Post>()
                    for (doc in task.result.documents) {
                        val post = doc.toObject(Post::class.java)
                        if (post != null) list.add(post)
                    }
                    listener.onSuccess(list)
                } else {
                    listener.onError(task.exception.toString())
                }
            }
    }

    override fun updatePost(post: Post, listener: Listenner<Boolean>) {
        Log.v("aaaaa", "on update post")
        Log.v("aaaaaa", post.id.toString())
        database.collection(Post.posts)
            .document(post.id.toString())
            .set(post)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) listener.onSuccess(true)
                else listener.onError(task.exception.toString())
            }
    }

    override fun getPost(id: String, listener: Listenner<Post>) {
        database.collection(Post.posts)
            .document(id)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val post = task.result.toObject(Post::class.java)
                    if (post != null) {
                        listener.onSuccess(post)
                    }
                }
            }
    }

    override fun getAllPost(listener: Listenner<List<Post>>) {
        database.collection(Post.posts)
            .whereIn(Post.type, listOf(Post.nhaChoThue, Post.phongChoThue, Post.phongOGhep))
            .whereEqualTo(Post.active, true)
            .whereEqualTo(Post.available, true)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = mutableListOf<Post>()
                    for (doc in task.result.documents) {
                        val post = doc.toObject(Post::class.java)
                        if (post != null) list.add(post)
                    }
                    listener.onSuccess(list)
                } else listener.onError(task.exception.toString())
            }
    }

    private fun getbyDistrict(lastIndex: Long, district: String, listener: Listenner<List<Post>>) {
        database.collection(Post.posts)
            .whereEqualTo("address.district", district.trim())
            .whereEqualTo(Post.active, true)
            .orderBy(Post.time, Query.Direction.DESCENDING)
            .whereLessThan(Post.time, lastIndex)
            .whereIn(Post.type, listOf(Post.phongChoThue, Post.phongOGhep, Post.nhaChoThue))
            .limit(20)
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
                    Log.v("aaaaa", "get : ${posts.size}")
                    listener.onSuccess(posts)
                }
            }
    }

    private fun getbyProvince(lastIndex: Long, province: String, listener: Listenner<List<Post>>) {
        database.collection(Post.posts)
            .whereEqualTo("address.city", province.trim())
            .whereEqualTo(Post.active, true)
            .orderBy(Post.time, Query.Direction.DESCENDING)
            .whereLessThan(Post.time, lastIndex)
            .whereIn(Post.type, listOf(Post.phongChoThue, Post.phongOGhep, Post.nhaChoThue))
            .limit(20)
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

    private fun addPost(post: Post, listener: Listenner<Boolean>) {
        post.time = System.currentTimeMillis()
        post.requestTime = System.currentTimeMillis()
        post.timeHired = System.currentTimeMillis()
        database.collection(Post.posts)
            .document(post.id.toString())
            .set(post)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) listener.onSuccess(true)
                else listener.onError(task.exception.toString())
            }
    }


}
