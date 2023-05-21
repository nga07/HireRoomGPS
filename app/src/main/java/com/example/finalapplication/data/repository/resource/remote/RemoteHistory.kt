package com.example.finalapplication.data.repository.resource.remote

import com.example.finalapplication.data.model.HistorySearch
import com.example.finalapplication.data.repository.resource.HistorySearchDataSource
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.getNewid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RemoteHistory : HistorySearchDataSource {

    private val auth = FirebaseAuth.getInstance()
    private val database = Firebase.firestore

    override fun addHistory(query: String) {
        val history = HistorySearch(query, System.currentTimeMillis(), auth.uid.toString())
        database.collection(HistorySearch.histories)
            .document(getNewid().toString())
            .set(history)
    }

    override fun getHistory(listenner: Listenner<List<HistorySearch>>) {
        database.collection(HistorySearch.histories)
            .whereEqualTo(HistorySearch.uid, auth.uid.toString())
            .orderBy(HistorySearch.time, Query.Direction.DESCENDING)
            .limit(7)
            .addSnapshotListener{v , e ->
                if(e!=null){
                    listenner.onError(e.toString())
                    return@addSnapshotListener
                }
                if(v!=null){
                    val list = mutableListOf<HistorySearch>()
                    for(doc in v.documents){
                        val history = doc.toObject(HistorySearch::class.java)
                        if(history!= null) list.add(history)
                    }
                    listenner.onSuccess(list)
                }
            }
    }
}
