package com.example.finalapplication.data.repository

import com.example.finalapplication.data.model.Invoice
import com.example.finalapplication.data.repository.resource.Listenner

interface InvoiceRepository {
    fun addInvoice(invoice: Invoice, listener: Listenner<Boolean>)
    fun getInvoice(listener: Listenner<List<Invoice>>)
}
