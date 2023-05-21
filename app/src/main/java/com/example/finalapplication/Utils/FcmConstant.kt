package com.example.finalapplication.utils

class FcmConstant {
    companion object {
        const val BASE_URL_FCM = "https://fcm.googleapis.com/fcm/"
        const val MEETING_TYPE = "meeting_type"
        const val DATA = "data"
        const val REGISTRATION_IDS = "registration_ids"
        const val INVITATION = "invitation"
        const val RESPOND_INVITATION = "respond"
        const val CANCEL_INVITATION = "cancel_invitation"
        const val ACCEPT_INVITATION = "accept_invitation"
        const val DENY_INVITATION = "deny_invitation"
        const val SERVER_KEY =
            "key=AAAAS_8NkEU:APA91bEl5fA1isKgJ7qJHOYvEqHQkUEVKnfwbghhTK63FaWsDqKd-" +
                    "Ja9glsJLm2W3JlTKSusGnYiVFQv5CGfDULZLd3sXCaS9dwz9rcR_eL-knWoGaiF8Co4yPKOeVOswBVvn0ZrGqBQ"
        const val AUTHORIZATION = "Authorization"
        const val CONTENT_TYPE = "Content-Type"
        const val CONTENT_TYPE_VALUE = "application/json"
        const val BASE_URL_MEET = "https://meet.jit.si"

        fun getHeader(): HashMap<String, String> {
            val headers = hashMapOf<String, String>()
            headers[AUTHORIZATION] = SERVER_KEY
            headers[CONTENT_TYPE] = CONTENT_TYPE_VALUE
            return headers
        }
    }

}
