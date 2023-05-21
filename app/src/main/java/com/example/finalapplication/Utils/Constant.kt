package com.example.finalapplication.utils

import com.example.finalapplication.R
import com.example.finalapplication.data.model.Utility
import java.util.*

object Constant {
    const val ERROR_USER_NOT_CONNECT = "Không thể liên lạc được"
    const val WARNING_ = "Warning!"
    const val ERROE_EMAIL_EMPTY = "enter your  email to reset password"
    const val NO_INTERNET = "No Internet, Please check!!"
    const val ROLE_CLIENT = "client"
    const val ROLE_STAFF = "staff"
    const val MSG_NO_DATA = "No data founded"
    const val ERROR_ = "Have error, please try late"
    const val ERROR_EMAIL_EXTIST = "Account Existed"
    const val ERROR_PASSWORD_SHORT = "Password need more 8 charactor"
    const val ERROR_CONFIRM_PASSWORD = "Confirm password different password"
    const val ERROR_ACCOUNT = "Account Incorrect"
    const val ERROR_USER = "Please Login"
    const val ERROR_NAME_EMPTY = "Name cannot be empty"
    const val ERROR_VALIDATE_MAIL = "Email is not validate"
    const val ERROR_INCORECT_PASSWORD = "Incorrect password"
    const val ERROR_ACCOUNT_EXIST = "Account Existed"
    const val MSG_SIGN_IN = "Sign in ...."
    const val MSG_CHECK_MAIL = "Please check your mail to get link reset password"
    const val MSG_UPDATE = "Update..."
    const val RECIVER = "cid"
    const val INTENT_POST = "POST"
    const val INTENT_USER = "USER"
    const val INTENT_TYPE_USER = "TYPE_USER"
    const val TYPE_IMAGE = "image/*"
    const val EXTENSION_IMAGE = ".jpg"
    const val PROVIDER = ".provider"
    const val SP_ALL = "Tất cả"
    const val SP_NEW = "Mới nhất"
    const val SP_PRICE_INCREMENT = "Giá tăng dần"
    const val SP_PRICE_DECREMENT = "Giá giảm dần"
    const val DATA_SEARCH = "DATA_SEARCH"
    const val INTENT_QUERY = "QUERY"
    const val INTENT_EMAIL = "EMAIL"
}

fun getNewid() = UUID.randomUUID()

fun getUtilities(): List<Utility> {
    val utilities = mutableListOf<Utility>()
    utilities.add(Utility(1, "Tủ lạnh", R.drawable.ic_fridge_35))
    utilities.add(Utility(2, "Điều hoà", R.drawable.ic_air_conditioner_35))
    utilities.add(Utility(3, "Máy nước nóng", R.drawable.ic_wardrobe_35))
    utilities.add(Utility(4, "WC riêng", R.drawable.ic_wc_50))
    utilities.add(Utility(5, "Nhà bếp", R.drawable.ic_cook_35))
    utilities.add(Utility(6, "Giường ngủ", R.drawable.ic_bed_35))
    utilities.add(Utility(7, "Nuôi thú cưng", R.drawable.ic_pets_35))
    utilities.add(Utility(8, "Không chung chủ", R.drawable.ic_key_35))
    utilities.add(Utility(9, "An ninh", R.drawable.ic_police_35))
    utilities.add(Utility(10, "Tivi", R.drawable.ic_television_35))
    utilities.add(Utility(11, "Cửa sổ", R.drawable.ic_window_80))
    utilities.add(Utility(12, "Wifi", R.drawable.ic_wifi_35))
    utilities.add(Utility(13, "Ban công", R.drawable.ic_balcony_35))
    utilities.add(Utility(14, "Gác lửng", R.drawable.ic_stair_35))
    return utilities
}
