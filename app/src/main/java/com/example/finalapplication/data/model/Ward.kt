package com.example.finalapplication.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Ward(
    @SerializedName("WardCode")
    val id : String?,
    @SerializedName("WardName")
    val name : String?
) : Parcelable