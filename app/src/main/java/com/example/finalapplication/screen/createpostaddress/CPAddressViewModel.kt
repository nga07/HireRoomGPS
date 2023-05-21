package com.example.finalapplication.screen.createpostaddress

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finalapplication.data.model.Address
import com.example.finalapplication.data.model.District
import com.example.finalapplication.data.model.Province
import com.example.finalapplication.data.model.Ward
import com.example.finalapplication.data.repository.AddressRepository
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.base.BaseViewModel

class CPAddressViewModel(val addressRepository: AddressRepository) : BaseViewModel() {

    private val _provinces = MutableLiveData<List<Province>>()
    val provinces: LiveData<List<Province>>
        get() = _provinces
    private val _districts = MutableLiveData<List<District>>()
    val districts: LiveData<List<District>>
        get() = _districts
    private val _wards = MutableLiveData<List<Ward>>()
    val ward: LiveData<List<Ward>>
        get() = _wards
    private val _street = MutableLiveData<Boolean>()
    val street: LiveData<Boolean>
        get() = _street
    private val _apartmentNumber = MutableLiveData<Boolean>()
    val apartmentNumber: LiveData<Boolean>
        get() = _apartmentNumber

    fun getProvinces() {
        addressRepository.getProvinces(object : Listenner<List<Province>> {
            override fun onSuccess(data: List<Province>) {
                _provinces.postValue(data)
            }

            override fun onError(msg: String) {
                // TODO("Not yet implemented")
            }

        })
    }

    fun getDistricts(province: Province) {
        addressRepository.getDistricts(province, object : Listenner<List<District>> {
            override fun onSuccess(data: List<District>) {
                for (i in data) {
                    _districts.postValue(data)
                }
            }

            override fun onError(msg: String) {
                Log.v("aaaaa", msg)
            }

        })
    }

    fun getWards(district: District) {
        addressRepository.getWards(district, object : Listenner<List<Ward>> {
            override fun onSuccess(data: List<Ward>) {
                _wards.postValue(data)
            }

            override fun onError(msg: String) {
                Log.v("aaaaa", msg)
            }

        })
    }

    fun validateInput(address: Address): Boolean {
        _apartmentNumber.value = !address.apartmentNumber.isNullOrEmpty()
        _street.value = !address.street.isNullOrEmpty()
        return _street.value!! && _apartmentNumber.value!!
    }
}