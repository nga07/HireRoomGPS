package com.example.finalapplication.data.repository

import com.example.finalapplication.data.model.Contact
import com.example.finalapplication.data.repository.resource.ContactDataSource
import com.example.finalapplication.data.repository.resource.Listenner

class ContactRepositoryImpl(private val remote: ContactDataSource.Remote) : ContactRepository {
    override suspend fun getListContact(lastIndex: Long, listen: Listenner<List<Contact>>) {
        remote.getListContact(lastIndex, listen)
    }

    override suspend fun getContact(id: String?, listen: Listenner<Contact>) {
        remote.getContact(id, listen)
    }

    override fun addNewContact(contact: Contact) {
        // TODO("Not yet implemented")
    }

    override fun updateSeenMessage(id: String?) {
        remote.updateSeenMessage(id)
    }
}
