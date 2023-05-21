package com.example.finalapplication.data.model

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable


class Post : Serializable {
    var id: String? = null
    var uid: String? = null
    var time: Long = 0
    var images: List<String>? = null
    var price: Long = 0
    var minPrice: Long = 0
    var maxPrice: Long = 0
    var postType: String? = null
    var numberOfRoom: Int = 0
    var address: Address = Address()
    var deposit: Long = 0
    var requireGender: Int? = null
    var maxOfPeople: Int = 0
    var peopleAvailable: Int = 0
    var utilities: List<UtilityPost>? = null
    var requestVerify: Boolean = false
    var verifying: Boolean = false
    var requestTime: Long = 0
    var verifyingTime: Long = 0
    var verify: Boolean? = null
    var squad: Int = 0
    var available: Boolean = true
    var open: String? = null
    var close: String? = null
    var title: String? = null
    var description: String? = null
    var active: Boolean = true
    var timeHired: Long = 0

    companion object {
        const val verifying = "verifying"
        const val verified = "verify"
        const val requireTime = "requestTime"
        const val verifyingTime = "verifyingTime"
        const val requestVerify = "requestVerify"
        const val uid = "uid"
        const val phongChoThue = "Phòng cho thuê"
        const val phongOGhep = "Phòng ở ghép"
        const val nhaChoThue = "Nhà cho thuê/ Căn hộ"
        const val timPhongO = "Tìm phòng ở"
        const val posts = "posts"
        const val type = "postType"
        const val time = "time"
        const val active = "active"
        const val available = "available"
        const val timeHired = "timeHired"
        val diffCallback = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun toString(): String {
        var x = id + " " + address + " " + timeHired + " "
        return x
    }
}
