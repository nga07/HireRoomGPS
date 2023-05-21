package com.example.finalapplication.data.repository.resource

import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.PostFavorite
import com.example.finalapplication.data.model.User

interface PostDataSource {
    fun addNewPost(post: Post, listener: Listenner<Boolean>)
    fun getNewPost(listener: Listenner<List<Post>>)
    fun getPostSearchRoom(lastIndex: Long, listener: Listenner<List<Pair<Post, User>>>)
    fun getPostRoomMate(lastIndex: Long, listener: Listenner<List<Post>>)
    fun getPostFavorite(lastIndex: Long, listener: Listenner<List<PostFavorite>>)
    fun addPostFavorite(post: Post)
    fun unFavoritePost(postFavorite: Post)
    fun getResultSearch(
        lastIndex: Long,
        ward: String,
        district: String,
        province: String,
        listener: Listenner<List<Post>>
    )

    fun getMypost(listener: Listenner<List<Post>>)
    fun requestVerifiy(post: Post, listener: Listenner<Boolean>)
    fun changeAcitivyPost(post: Post, listener: Listenner<Boolean>)
    fun deletePost(post: Post, listener: Listenner<Boolean>)
    fun getPostRequire(lastIndex: Long, listener: Listenner<List<Post>>)
    fun getPostVerifying(lastIndex: Long, listener: Listenner<List<Post>>)
    fun updatePost(post: Post, listener: Listenner<Boolean>)
    fun getPost(id: String, listener: Listenner<Post>)
    fun getAllPost(listener: Listenner<List<Post>>)
}
