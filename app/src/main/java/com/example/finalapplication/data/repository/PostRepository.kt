package com.example.finalapplication.data.repository

import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.PostFavorite
import com.example.finalapplication.data.model.User
import com.example.finalapplication.data.repository.resource.Listenner

interface PostRepository {
    fun addPost(post: Post, listener: Listenner<Boolean>)
    fun getNewPost(listener: Listenner<List<Post>>)
    fun getPostSearch(lastIndex: Long, listener: Listenner<List<Pair<Post, User>>>)
    fun getPostRoomate(listener: Listenner<List<Post>>)
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
    fun getMyPost(listener: Listenner<List<Post>>)
    fun requestVerifiy(post : Post, listener: Listenner<Boolean>)
    fun changeAcitivyPost(post: Post, listener: Listenner<Boolean>)
    fun deletePost(post: Post, listener: Listenner<Boolean>)
    fun getPostRequire(lastIndex: Long,listener: Listenner<List<Post>>)
    fun getPostVerifying(lastIndex: Long,listener: Listenner<List<Post>>)
    fun updatePost(post: Post, listener: Listenner<Boolean>)
    fun getPost(id : String, listener: Listenner<Post>)
    fun getAllPost(listener: Listenner<List<Post>>)
}
