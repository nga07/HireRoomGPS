package com.example.finalapplication.data.repository

import com.example.finalapplication.data.model.Post
import com.example.finalapplication.data.model.PostFavorite
import com.example.finalapplication.data.model.User
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.data.repository.resource.PostDataSource

class PostRepositoryImpl(private val remote: PostDataSource) : PostRepository {

    override fun addPost(post: Post, listener: Listenner<Boolean>) {
        remote.addNewPost(post, listener)
    }

    override fun getNewPost(listener: Listenner<List<Post>>) {
        remote.getNewPost(listener)
    }

    override fun getPostSearch(lastIndex: Long, listener: Listenner<List<Pair<Post, User>>>) {
        remote.getPostSearchRoom(lastIndex, listener)
    }

    override fun getPostRoomate(listener: Listenner<List<Post>>) {
        remote.getPostRoomMate(Long.MAX_VALUE, listener)
    }

    override fun getPostFavorite(lastIndex: Long, listener: Listenner<List<PostFavorite>>) {
        remote.getPostFavorite(lastIndex, listener)
    }

    override fun addPostFavorite(post: Post) {
        remote.addPostFavorite(post)
    }

    override fun unFavoritePost(postFavorite: Post) {
        remote.unFavoritePost(postFavorite)
    }

    override fun getResultSearch(
        lastIndex: Long,
        ward: String,
        district: String,
        province: String,
        listener: Listenner<List<Post>>
    ) {
        remote.getResultSearch(lastIndex, ward, district, province, listener)
    }

    override fun getMyPost(listener: Listenner<List<Post>>) {
        remote.getMypost(listener)
    }

    override fun requestVerifiy(post: Post, listener: Listenner<Boolean>) {
        remote.requestVerifiy(post, listener)
    }

    override fun changeAcitivyPost(post: Post, listener: Listenner<Boolean>) {
        remote.changeAcitivyPost(post, listener)
    }

    override fun deletePost(post: Post, listener: Listenner<Boolean>) {
        remote.deletePost(post, listener)
    }

    override fun getPostRequire(lastIndex: Long, listener: Listenner<List<Post>>) {
        remote.getPostRequire(lastIndex, listener)
    }

    override fun getPostVerifying(lastIndex: Long, listener: Listenner<List<Post>>) {
        remote.getPostVerifying(lastIndex, listener)
    }

    override fun updatePost(post: Post, listener: Listenner<Boolean>) {
        remote.updatePost(post, listener)
    }

    override fun getPost(id: String, listener: Listenner<Post>) {
        remote.getPost(id, listener)
    }

    override fun getAllPost(listener: Listenner<List<Post>>) {
        remote.getAllPost(listener)
    }
}
