package com.example.finalapplication.utils

object ApiConstant {
    const val BASE_URL_GHN = "https://online-gateway.ghn.vn/shiip/public-api/master-data/"

    fun getHeader(): HashMap<String, String> {
        val headers = hashMapOf<String, String>()
        headers["token"] = "5da343cd-46bf-11ed-8008-c673db1cbf27"
        headers["Content-Type"] = "application/json"
        return headers
    }
}