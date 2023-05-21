package com.example.finalapplication.utils

import com.example.finalapplication.data.model.Post

interface FavoriteListener {
    fun onclick(post: Post)
}