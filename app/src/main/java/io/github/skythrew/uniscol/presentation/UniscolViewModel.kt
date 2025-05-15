package io.github.skythrew.uniscol.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.skythrew.uniscol.data.accounts.AccountRepository
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountRepository
import io.github.skythrew.uniscol.data.network.NetworkRepository
import javax.inject.Inject

@HiltViewModel
class UniscolViewModel @Inject constructor(
    val networkRepository: NetworkRepository,
    private val accountsRepository: AccountRepository,
    private val restaurantAccountsRepository: RestaurantAccountRepository
) : ViewModel() {
    val accounts = accountsRepository.getAllAccountsStream()
    val restaurantAccounts = restaurantAccountsRepository.getAllAccountsStream()
}