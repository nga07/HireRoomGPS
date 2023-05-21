package com.example.finalapplication.utils

import com.example.finalapplication.data.model.District
import com.example.finalapplication.data.model.Province
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface ApiGhn {
    @POST("district")
    fun getDistricts(
        @HeaderMap headers: HashMap<String, String>,
        @Body body: Province
    ): Call<Province>

    @POST("ward")
    fun getWards(
        @HeaderMap headers: HashMap<String, String>,
        @Body body: District
    ): Call<District>
}