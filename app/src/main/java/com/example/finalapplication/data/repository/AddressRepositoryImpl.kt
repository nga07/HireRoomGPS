package com.example.finalapplication.data.repository

import com.example.finalapplication.data.model.District
import com.example.finalapplication.data.model.Province
import com.example.finalapplication.data.model.Ward
import com.example.finalapplication.data.repository.resource.Listenner
import com.example.finalapplication.utils.ApiConstant
import com.example.finalapplication.utils.ApiGhn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressRepositoryImpl(val apiGhn: ApiGhn) : AddressRepository {

    override fun getProvinces(listener: Listenner<List<Province>>) {
        listener.onSuccess(mutableListOf(Province(201, "Hà Nội", null)))
    }

    override fun getDistricts(province: Province, listener: Listenner<List<District>>) {
        apiGhn.getDistricts(ApiConstant.getHeader(), province).enqueue(object : Callback<Province> {
            override fun onResponse(call: Call<Province>, response: Response<Province>) {
                val districts = response.body()?.districts
                districts?.let { listener.onSuccess(it) }
            }

            override fun onFailure(call: Call<Province>, t: Throwable) {
                listener.onError(t.toString())
            }

        })
    }

    override fun getWards(district: District, listener: Listenner<List<Ward>>) {
        apiGhn.getWards(ApiConstant.getHeader(), district).enqueue(object : Callback<District> {
            override fun onResponse(call: Call<District>, response: Response<District>) {
                val wards = response.body()?.wards
                wards?.let { listener.onSuccess(it) }
            }

            override fun onFailure(call: Call<District>, t: Throwable) {
                listener.onError(t.toString())
            }

        })
    }
}