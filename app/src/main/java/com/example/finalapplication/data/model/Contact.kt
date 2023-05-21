package com.example.finalapplication.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

@Parcelize
class Contact() : Parcelable {
    var id: String? = null
    var uid: String? = null
    var adversaryUser: User? = null
    var message: Message? = null

    constructor(id: String, uid: String, adversary: User, message: Message) : this() {
        this.id = id
        this.uid = uid
        this.adversaryUser = adversary
        this.message = message
    }

    companion object {
        const val contacts = "contacts"
        const val uid = "uid"
        const val id = "id"
        const val message = "message"
        val diffCallback = object : DiffUtil.ItemCallback<Contact>() {
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem == newItem
            }

        }
    }
}
