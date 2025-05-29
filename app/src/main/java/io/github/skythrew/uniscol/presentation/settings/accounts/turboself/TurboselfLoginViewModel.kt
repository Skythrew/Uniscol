package io.github.skythrew.uniscol.presentation.settings.accounts.turboself

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.skythrew.turboselfkt.core.TurboselfClient
import io.github.skythrew.uniscol.data.accounts.Account
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountFeature
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountInfos
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountRepository
import io.github.skythrew.uniscol.data.services.ServiceType
import io.github.skythrew.uniscol.data.services.Services
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TurboselfLoginViewModel @Inject constructor (private val restaurantAccountRepository: RestaurantAccountRepository): ViewModel() {
    private val client = TurboselfClient()

    var _loginError = MutableStateFlow(false)
    val loginError = _loginError

    var _isLogging = MutableStateFlow(false)
    val isLogging = _isLogging

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLogging.value = true
            try {
                val authInfos = client.loginWithCredentials(username, password)
                val host = client.host()

                restaurantAccountRepository.insertAccount(Account(
                    service = Services.Turboself,
                    label = host.firstName + ' ' + host.lastName,
                    username = authInfos.username,
                    password = authInfos.password,
                    accessToken = authInfos.accessToken.value,
                    accessTokenExpiration = authInfos.accessToken.expirationDate.toEpochMilliseconds(),
                    refreshToken = authInfos.refreshToken.value,
                    refreshTokenExpiration = authInfos.refreshToken.expirationDate.toEpochMilliseconds(),
                    supportCanteen = true,
                    supportConversation = false
                ), infos = RestaurantAccountInfos(
                    accountId = 0,
                    cardNumber = host.cardNumber.toString(),
                    features = listOf(RestaurantAccountFeature.QRCode, RestaurantAccountFeature.Booking)
                ))

                _loginError.value = false
            } catch (e: Exception) {
                _loginError.value = true
                Log.e("ERR", e.message.toString())
            }
            _isLogging.value = false
        }
    }
}