package com.example.finalapplication.data.repository.resource

import com.example.finalapplication.data.model.HistorySearch

interface HistorySearchDataSource {

    fun addHistory(query : String)
    fun getHistory(listenner: Listenner<List<HistorySearch>>)
}
