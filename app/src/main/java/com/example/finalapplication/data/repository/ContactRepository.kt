package com.example.finalapplication.data.repository

import com.example.finalapplication.data.model.Contact
import com.example.finalapplication.data.repository.resource.Listenner

interface ContactRepository {
    suspend fun getListContact(lastIndex: Long, listen: Listenner<List<Contact>>)
    suspend fun getContact(id: String?, listen: Listenner<Contact>)
    fun addNewContact(contact: Contact)
    fun updateSeenMessage(id: String?)
}
