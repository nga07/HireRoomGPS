package com.example.finalapplication.data.repository.resource.remote

import com.example.finalapplication.data.model.Contact
import com.example.finalapplication.data.model.Message
import com.example.finalapplication.data.repository.resource.ContactDataSource
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.NumberConstant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RemoteContact : ContactDataSource.Remote {
    private val database = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()

    override suspend fun getListContact(lastIndex: Long, listen: Listenner<List<Contact>>) {
        val index = if (lastIndex == 0L) Long.MAX_VALUE
        else lastIndex
        database.collection(Contact.contacts)
            .whereEqualTo(Contact.uid, auth.uid)
            .orderBy("${Contact.message}.${Message.time}", Query.Direction.DESCENDING)
            .whereLessThan("${Contact.message}.${Message.time}", index)
            .limit(NumberConstant.ITEM_PER_PAGE.toLong())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listen.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val contacts = mutableListOf<Contact>()
                    for (doc in value.documents) {
                        val contact = doc.toObject(Contact::class.java)
                        contact?.let { contacts.add(it) }
                    }
                    listen.onSuccess(contacts)
                }
            }
    }

    override suspend fun getContact(id: String?, listen: Listenner<Contact>) {
        database.collection(Contact.contacts)
            .whereEqualTo(Contact.id, auth.uid + id)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listen.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null && value.isEmpty.not()) {
                    val contact = value.documents.first().toObject(Contact::class.java)
                    contact?.let { listen.onSuccess(it) }
                }
            }
    }

    override fun addNewContact(contact: Contact) {
        database.collection(Contact.contacts)
            .document(contact.id.toString())
            .set(contact)
    }

    override fun updateSeenMessage(id: String?) {
        database.collection(Contact.contacts)
            .document(id.toString())
            .update(mapOf("${Contact.message}.${Message.seen}" to true))
    }
}
