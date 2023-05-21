package com.example.finalapplication.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Province(
    val province_id : Int,
    val name : String?,
    @SerializedName("data")
    val districts : List<District>?
) : Parcelable