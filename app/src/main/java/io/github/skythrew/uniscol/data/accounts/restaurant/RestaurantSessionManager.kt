package io.github.skythrew.uniscol.data.accounts.restaurant

import android.util.Log
import io.github.skythrew.uniscol.data.network.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class RestaurantSession(
    val account: RestaurantAccountInterface,
    var balance: Int? = null
)

@Singleton
class RestaurantSessionManager @Inject constructor(
    val networkRepository: NetworkRepository
) {
    val _currentAccount = MutableStateFlow<RestaurantAccountInterface?>(null)
    val currentAccount: StateFlow<RestaurantAccountInterface?> = _currentAccount

    val _currentSession = MutableStateFlow<RestaurantSession?>(null)
    val currentSession: StateFlow<RestaurantSession?> = _currentSession

    private val authenticatedAccounts = HashMap<Int, RestaurantSession>()

    fun setCurrentAccount(account: RestaurantAccountInterface) {
        if (!authenticatedAccounts.containsKey(account.id)) {
            Log.d("LOG", account.username.toString())
            authenticatedAccounts[account.id] = RestaurantSession(account)
        }

        _currentSession.value = authenticatedAccounts[account.id]!!
        _currentAccount.value = authenticatedAccounts[account.id]!!.account
    }

    suspend fun login() {
        if (networkRepository.online.value && _currentAccount.value != null)
            _currentAccount.value!!.login()
    }

    suspend fun fetchBalance(): Int? {
        if (_currentAccount.value == null) {
            Log.e("DATA ERROR", "Cannot fetch balance of unknown account")
            return null
        }
        else {
            try {
                if (!_currentAccount.value!!.loggedIn)
                    _currentAccount.value!!.login()

                _currentSession.value!!.balance = _currentAccount.value!!.getBalance()
            } catch (e: Exception) {
                _currentSession.value!!.balance = null
            }

            return _currentSession.value!!.balance
        }
    }
}