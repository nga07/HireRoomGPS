package com.example.finalapplication.screen.historycontact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.Contact
import com.example.finalapplication.data.repository.ContactRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.base.BaseViewModel

class HistoryContactViewModel(private val contactRepository: ContactRepository) :
    BaseViewModel() {

    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>>
        get() = _contacts

    init {
        getListContact(0L)
    }

    fun getListContact(lastIndex: Long) {
        launchTask<List<Contact>>(onRequest = {
            contactRepository.getListContact(lastIndex, object : Listenner<List<Contact>> {
                override fun onSuccess(data: List<Contact>) {
                    _contacts.value = data
                    hideLoading(true)
                }

                override fun onError(msg: String) {
                    message.value = msg
                    hideLoading(true)
                }
            })
        })
    }
}
