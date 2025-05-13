package io.github.skythrew.uniscol.presentation.restaurant

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.skythrew.uniscol.data.accounts.AccountRepository
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountInterface
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountRepository
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantSessionManager
import io.github.skythrew.uniscol.data.services.ServiceType
import io.github.skythrew.uniscol.presentation.UniscolViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.first as first1

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    val sessionManager: RestaurantSessionManager,
    val accountsRepository: RestaurantAccountRepository
): ViewModel() {
    val accounts = accountsRepository.getAllAccountsStream()

    val selectedAccount = sessionManager.currentAccount
    val session = sessionManager.currentSession

    private val _balance = MutableStateFlow<Int?>(null)
    val balance = _balance

    var initialized = false

    init {
        viewModelScope.launch {
            if (selectedAccount.value == null)
                sessionManager.setCurrentAccount(accounts.first1()[0])

            selectedAccount.collect { account ->
                if (account != null) {
                    _balance.value = sessionManager.fetchBalance()

                    _cardNumber.value = account.cardNumber

                    Log.d("RestaurantViewModel", "Balance updated to ${sessionManager.currentSession.value!!.balance}")
                }
            }
        }
    }

    var _cardNumber = MutableStateFlow<String?>(null)
    val cardNumber = _cardNumber

    fun updateSelectedAccount(account: RestaurantAccountInterface) {
        viewModelScope.launch {
            sessionManager.setCurrentAccount(account)
        }
    }
}