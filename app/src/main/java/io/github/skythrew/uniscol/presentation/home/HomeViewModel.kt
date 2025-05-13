package io.github.skythrew.uniscol.presentation.home

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import io.github.skythrew.uniscol.data.accounts.Account
import io.github.skythrew.uniscol.data.accounts.AccountInterface
import io.github.skythrew.uniscol.data.accounts.AccountRepository
import io.github.skythrew.uniscol.data.services.ServiceType
import io.github.skythrew.uniscol.data.services.Services
import io.github.skythrew.uniscol.presentation.UniscolViewModel
import io.github.skythrew.uniscol.presentation.navigation.SettingsTab

class HomeViewModel (
    val appViewModel: UniscolViewModel,
    private val navController: NavController
): ViewModel() {
    fun navigateToSettings() {
        navController.navigate(SettingsTab)
    }
}