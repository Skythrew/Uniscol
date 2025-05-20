package io.github.skythrew.uniscol.presentation.settings.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.skythrew.uniscol.data.accounts.Account
import io.github.skythrew.uniscol.data.accounts.AccountRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountsSettingsViewModel @Inject constructor (private val accountsRepository: AccountRepository): ViewModel() {
    val accounts get() = accountsRepository.getAllAccountsStream()

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            accountsRepository.deleteAccount(account)
        }
    }
}