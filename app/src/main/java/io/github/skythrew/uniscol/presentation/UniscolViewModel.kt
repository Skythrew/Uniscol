package io.github.skythrew.uniscol.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.skythrew.uniscol.data.accounts.AccountRepository
import io.github.skythrew.uniscol.data.accounts.conversation.ConversationAccountRepository
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountRepository
import io.github.skythrew.uniscol.data.navigation.TabRepository
import io.github.skythrew.uniscol.data.network.NetworkRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UniscolViewModel @Inject constructor(
    val networkRepository: NetworkRepository,
    private val accountsRepository: AccountRepository,
    private val conversationAccountRepository: ConversationAccountRepository,
    private val restaurantAccountsRepository: RestaurantAccountRepository,
    private val tabsRepository: TabRepository
) : ViewModel() {
    val accounts = accountsRepository.getAllAccountsStream()
    val restaurantAccounts = restaurantAccountsRepository.getAllAccountsStream()
    val conversationAccounts = conversationAccountRepository.getAllAccountsStream()
    val tabs = tabsRepository.getAllTabsStream()

    fun disableTab(destination: String) {
        viewModelScope.launch {
            tabsRepository.disableDestination(destination)
        }
    }

    fun enableTab(destination: String) {
        viewModelScope.launch {
            tabsRepository.enableDestination(destination)
        }
    }
}