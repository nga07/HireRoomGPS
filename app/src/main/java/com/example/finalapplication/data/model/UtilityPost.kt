package com.example.finalapplication.data.model

import java.io.Serializable

class UtilityPost() : Serializable {
    var utility : Utility? = Utility()
    var price : Long?= 0
    constructor(utility: Utility, price : Long) :this(){
        this.utility = utility
        this.price = price
    }
}