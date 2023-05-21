package com.example.finalapplication.data.repository.resource

import com.example.finalapplication.data.model.Contact

interface ContactDataSource {
    interface Remote {
        suspend fun getListContact(lastIndex: Long, listen: Listenner<List<Contact>>)
        suspend fun getContact(id: String?, listen: Listenner<Contact>)
        fun addNewContact(contact: Contact)
        fun updateSeenMessage(id: String?)
    }
}
