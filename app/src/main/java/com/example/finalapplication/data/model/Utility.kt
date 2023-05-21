package com.example.finalapplication.data.model

import java.io.Serializable

class Utility() : Serializable {
    var id: Int? = null
    var name: String? = null
    var icon: Int? = null

    constructor(id: Int, name: String, icon: Int?) : this() {
        this.name = name
        this.id = id
        this.icon = icon
    }
}