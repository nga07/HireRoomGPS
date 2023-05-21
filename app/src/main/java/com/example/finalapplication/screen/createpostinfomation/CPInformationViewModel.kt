package com.example.finalapplication.screen.createpostinfomation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.utils.base.BaseViewModel

class CPInformationViewModel : BaseViewModel() {
    private val _personPerRoom = MutableLiveData<Boolean>()
    val personPerRoom: LiveData<Boolean>
        get() = _personPerRoom

    private val _available = MutableLiveData<Boolean>()
    val available: LiveData<Boolean>
        get() = _available

    private val _numberOfRoom = MutableLiveData<Boolean>()
    val numberOfRoom: LiveData<Boolean>
        get() = _numberOfRoom

    private val _squad = MutableLiveData<Boolean>()
    val squad: LiveData<Boolean>
        get() = _squad

    private val _price = MutableLiveData<Boolean>()
    val price: LiveData<Boolean>
        get() = _price

    private val _deposit = MutableLiveData<Boolean>()
    val deposit: LiveData<Boolean>
        get() = _deposit

    fun validateInput(post: Post): Boolean {
        _numberOfRoom.value = post.maxOfPeople != 0
        _deposit.value = post.deposit != 0L
        _squad.value = post.squad != 0
        _price.value = post.price != 0L
        _personPerRoom.value = post.maxOfPeople != 0
        var result = personPerRoom.value!! && squad.value!! && price.value!! && deposit.value!!
        if (post.postType == Post.phongOGhep) {
            _available.value = post.peopleAvailable != 0
            result = result && available.value!!
        }
        if (post.postType == Post.nhaChoThue) {
            _numberOfRoom.value = post.numberOfRoom != 0
            result = result && numberOfRoom.value!!
        }
        return result
    }
}