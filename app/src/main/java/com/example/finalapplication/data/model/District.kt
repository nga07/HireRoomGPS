package com.example.finalapplication.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class District(
    @SerializedName(value = "district_id", alternate = ["DistrictID"])
    val district_id : Int?,
    @SerializedName("DistrictName")
    val name : String?,
    @SerializedName("data")
    val wards : List<Ward>?
) : Parcelable