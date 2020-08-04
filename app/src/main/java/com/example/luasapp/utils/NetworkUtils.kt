package com.example.luasapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest

interface NetworkListener {
    fun onConnectionAvailable(isConnected: Boolean)
}

class CheckNetwork(
    private val context: Context,
    private val listener: NetworkListener?
) {

    init {
        registerNetworkCallback()
    }

    private fun registerNetworkCallback() {
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val builder = NetworkRequest.Builder()

            connectivityManager.registerNetworkCallback(
                builder.build(),
                object : NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        listener?.onConnectionAvailable(true)
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        listener?.onConnectionAvailable(false)
                    }
                })
        } catch (e: Exception) {
            listener?.onConnectionAvailable(false)
        }
    }
}