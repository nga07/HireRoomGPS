package com.example.finalapplication.data.repository

import com.example.finalapplication.data.model.HistorySearch
import com.example.finalapplication.data.repository.resource.HistorySearchDataSource
import com.example.finalapplication.data.repository.resource.Listenner

class HistoryRepositoryImpl(private val remote : HistorySearchDataSource): HistoryRepository {
    override fun addHistory(query: String) {
        remote.addHistory(query)
    }

    override fun getHistory(listenner: Listenner<List<HistorySearch>>) {
        remote.getHistory(listenner)
    }
}
