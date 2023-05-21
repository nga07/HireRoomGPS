package com.example.finalapplication.data.model

import java.io.Serializable

class Invoice() : Serializable {
    var id : String? = null
    var uid : String? =null
    var dateOn : String? = null
    var phone : String? = null
    var post : Post? = null
    var deposit : Float = 1000000f
    var timePay : Long = 0

    constructor(id : String, dateOn : String, phone : String, post : Post) : this(){
        this.id = id
        this.uid = uid
        this.dateOn = dateOn
        this.phone = phone
        this.post = post
    }

    companion object{
        const val uid = "uid"
        const val invoices = "invoices"
        const val time = "timePay"
    }
}
