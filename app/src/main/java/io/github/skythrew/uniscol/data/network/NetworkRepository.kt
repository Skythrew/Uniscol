package io.github.skythrew.uniscol.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
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
        connectivityManager?.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _online.value = true
            }

            override fun onLost(network: Network) {
                _online.value = false
            }
        })
    }
}