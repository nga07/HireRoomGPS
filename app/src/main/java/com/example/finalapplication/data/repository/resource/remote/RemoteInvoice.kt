package com.example.finalapplication.data.repository.resource.remote

import com.example.finalapplication.data.model.Invoice
import com.example.finalapplication.data.repository.resource.InvoiceDataSource
import com.example.finalapplication.data.repository.resource.Listenner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RemoteInvoice : InvoiceDataSource {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = Firebase.firestore

    override fun addInvoice(invoice: Invoice, listener: Listenner<Boolean>) {
        invoice.uid = auth.uid
        invoice.timePay = System.currentTimeMillis()
        database.collection(Invoice.invoices)
            .add(invoice)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) listener.onSuccess(true)
                else listener.onError(task.exception.toString())
            }
    }

    override fun getInvoice(listener: Listenner<List<Invoice>>) {
        database.collection(Invoice.invoices)
            .orderBy(Invoice.time, Query.Direction.DESCENDING)
            .whereEqualTo(Invoice.uid, auth.uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    listener.onError(error.toString())
                    return@addSnapshotListener
                }
                if (value != null) {
                    val listBill = mutableListOf<Invoice>()
                    for (doc in value.documents) {
                        val bill = doc.toObject(Invoice::class.java)
                        if (bill != null) listBill.add(bill)
                    }
                    listener.onSuccess(listBill)
                }
            }
    }
}
