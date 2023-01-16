package com.example.bibleapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

interface ConnectivityStateObserver{
    fun observe(): Flow<ConnectivityState>

    enum class ConnectivityState{
        AVAILABLE, UNAVAILABLE, LOSING, LOST
    }
}
class ConnectivityStateObserverImpl(context: Context): ConnectivityStateObserver{
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    override fun observe(): Flow<ConnectivityStateObserver.ConnectivityState> {
        return callbackFlow {
            val callback = object: ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch {send(ConnectivityStateObserver.ConnectivityState.AVAILABLE)}
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch {send(ConnectivityStateObserver.ConnectivityState.LOSING)}
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch {send(ConnectivityStateObserver.ConnectivityState.LOST)}
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch {send(ConnectivityStateObserver.ConnectivityState.UNAVAILABLE)}
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose{
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }

}