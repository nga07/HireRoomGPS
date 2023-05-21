package com.example.finalapplication.data.repository

import com.example.finalapplication.data.model.HistorySearch
import com.example.finalapplication.data.repository.resource.Listenner

interface HistoryRepository {

    fun addHistory(query : String)
    fun getHistory(listenner: Listenner<List<HistorySearch>>)
}
