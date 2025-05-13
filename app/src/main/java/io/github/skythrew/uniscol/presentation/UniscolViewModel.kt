package io.github.skythrew.uniscol.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.skythrew.uniscol.data.accounts.AccountRepository
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class UniscolViewModel @Inject constructor(
    private val accountsRepository: AccountRepository,
    private val restaurantAccountsRepository: RestaurantAccountRepository
) : ViewModel() {
    val accounts = accountsRepository.getAllAccountsStream()
    val restaurantAccounts = restaurantAccountsRepository.getAllAccountsStream()

    private val _offline = MutableStateFlow(false)
    val offline = _offline

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _offline.value = false
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            _offline.value = true
        }
    }
}