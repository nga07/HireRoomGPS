package com.example.finalapplication.data.repository

import com.example.finalapplication.data.model.District
import com.example.finalapplication.data.model.Province
import com.example.finalapplication.data.model.Ward
import com.example.finalapplication.data.repository.resource.Listenner

interface AddressRepository {
    fun getProvinces(listener : Listenner<List<Province>>)
    fun getDistricts(province: Province, listener : Listenner<List<District>>)
    fun getWards(district: District,listener : Listenner<List<Ward>>)
}