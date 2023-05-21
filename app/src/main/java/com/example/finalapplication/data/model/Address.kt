package com.example.finalapplication.data.model

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

class Address() : Serializable {
    var city: String? = null
    var district: String? = null
    var ward: String? = null
    var street: String? = null
    var apartmentNumber: String? = null
    var latitude : Double? =null
    var longitude : Double? = null

    constructor(
        city: String?,
        district: String?,
        ward: String?,
        street: String?,
        apartmentNumber: String?
    ) : this() {
        this.city = city
        this.district = district
        this.ward = ward
        this.street = street
        this.apartmentNumber = apartmentNumber
    }

    override fun toString(): String {
        return "$apartmentNumber, $street, $ward, $district, $city"
    }

    companion object {
        const val city = "city"
        const val district = "district"
        const val ward = "ward"
    }

}
