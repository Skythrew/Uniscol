package io.github.skythrew.uniscol.presentation.restaurant

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountInterface
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountRepository
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantSessionManager
import io.github.skythrew.uniscol.data.network.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.first as first1

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    val networkRepository: NetworkRepository,
    val sessionManager: RestaurantSessionManager,
    val accountsRepository: RestaurantAccountRepository
): ViewModel() {
    val accounts = accountsRepository.getAllAccountsStream()

    val selectedAccount = sessionManager.currentAccount
    val session = sessionManager.currentSession

    private val _balance = MutableStateFlow<Int?>(null)
    val balance = _balance

    private val _cardNumber = MutableStateFlow<String?>(null)
    val cardNumber = _cardNumber

    init {
        viewModelScope.launch {
            if (selectedAccount.value == null)
                sessionManager.setCurrentAccount(accounts.first1()[0])

            _cardNumber.value = sessionManager.currentAccount.value!!.cardNumber

            selectedAccount.collect { account ->
                if (account != null) {
                    try {
                        _balance.value = sessionManager.fetchBalance()
                    } catch (e: Exception) {
                        _balance.value = null
                    }

                    Log.d("RestaurantViewModel", "Balance updated to ${sessionManager.currentSession.value!!.balance}")
                }
            }
        }
    }

    fun updateSelectedAccount(account: RestaurantAccountInterface) {
        viewModelScope.launch {
            sessionManager.setCurrentAccount(account)
        }
    }
}