package com.example.finalapplication.data.model

class PostFavorite() {
    var id: String? = null
    var uid: String? = null
    var time: Long? = null
    var post: Post? = Post()

    constructor(id: String, uid: String, time: Long, post: Post) : this() {
        this.id = id
        this.uid = uid
        this.time = time
        this.post = post
    }
    companion object{
        const val name = "postFavorites"
        const val time = "time"
        const val uid = "uid"
    }
}