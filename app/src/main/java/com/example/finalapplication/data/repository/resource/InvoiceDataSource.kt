package com.example.finalapplication.data.repository.resource

import com.example.finalapplication.data.model.Invoice

interface InvoiceDataSource {
    fun addInvoice(invoice: Invoice, listener: Listenner<Boolean>)
    fun getInvoice(listener: Listenner<List<Invoice>>)
}
