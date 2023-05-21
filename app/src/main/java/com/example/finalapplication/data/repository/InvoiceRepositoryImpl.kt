package com.example.finalapplication.data.repository

import com.example.finalapplication.data.model.Invoice
import com.example.finalapplication.data.repository.resource.InvoiceDataSource
import com.example.finalapplication.data.repository.resource.Listenner

class InvoiceRepositoryImpl(val remote : InvoiceDataSource) : InvoiceRepository {
    override fun addInvoice(invoice: Invoice, listener: Listenner<Boolean>) {
        remote.addInvoice(invoice, listener)
    }

    override fun getInvoice(listener: Listenner<List<Invoice>>) {
        remote.getInvoice(listener)
    }
}
