package com.example.finalapplication.data.model

class HistorySearch() {
    var query : String? = null
    var time : Long = 0
    var uid : String? = null

    constructor(query : String, time : Long, uid :String) : this(){
        this.query = query
        this.time = time
        this.uid = uid
    }

    companion object{
        const val histories = "histories"
        const val time = "time"
        const val uid ="uid"
    }
}
