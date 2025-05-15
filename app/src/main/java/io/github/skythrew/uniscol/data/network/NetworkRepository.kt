package io.github.skythrew.uniscol.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val connectivityManager = getSystemService(context, ConnectivityManager::class.java)
    private val currentNetwork = connectivityManager?.activeNetwork

    private val _online = MutableStateFlow(currentNetwork != null)
    val online = _online

    init {
        Log.d("BONJOUR", "JE MAPPELLE JEAN CLAUDE")
        connectivityManager?.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network : Network) {
                Log.e("A", "The default network is now: " + network)
                _online.value = true
            }

            override fun onLost(network : Network) {
                Log.e("B", "The application no longer has a default network. The last default network was " + network)
                _online.value = false
            }

            override fun onCapabilitiesChanged(network : Network, networkCapabilities : NetworkCapabilities) {
                Log.e("C", "The default network changed capabilities: " + networkCapabilities)
            }

            override fun onLinkPropertiesChanged(network : Network, linkProperties : LinkProperties) {
                Log.e("D", "The default network changed link properties: " + linkProperties)
            }
        })
    }
}