package io.github.skythrew.uniscol.presentation.settings.accounts

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.skythrew.uniscol.data.accounts.AccountRepository
import javax.inject.Inject

@HiltViewModel
class AccountsSettingsViewModel @Inject constructor (private val accountsRepository: AccountRepository): ViewModel() {
    val accounts get() = accountsRepository.getAllAccountsStream()
}