package com.example.finalapplication.utils

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import com.example.finalapplication.databinding.DialogNoInternetBinding

class NetworkUtils(private val connectManager: ConnectivityManager) : LiveData<Boolean>() {

    private val connectCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }
    }

    override fun onActive() {
        super.onActive()
        val builder = NetworkRequest.Builder()
        connectManager.registerNetworkCallback(builder.build(), connectCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectManager.unregisterNetworkCallback(connectCallback)
    }

    companion object {
        fun getDialogNoInternet(
            context: Context?,
            layoutInflater: LayoutInflater,
            view: ViewGroup
        ): AlertDialog {
            val dialogNointernet = AlertDialog.Builder(context).create()
            dialogNointernet.setView(
                DialogNoInternetBinding.inflate(
                    layoutInflater,
                    view,
                    false
                ).root
            )
            dialogNointernet.setCancelable(false)
            dialogNointernet.setTitle(Constant.WARNING_)
            return dialogNointernet
        }
    }
}
