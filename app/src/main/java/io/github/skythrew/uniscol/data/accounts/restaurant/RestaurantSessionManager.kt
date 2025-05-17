package io.github.skythrew.uniscol.data.accounts.restaurant

import io.github.skythrew.uniscol.data.dates.isoWeekNumber
import io.github.skythrew.uniscol.data.network.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

data class RestaurantSession(
    val account: RestaurantAccountInterface,
    var balance: Int? = null,
    var bookings: HashMap<String, List<Booking>> = HashMap()
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

    suspend fun fetchBookings(date: String, forceRefresh: Boolean = false): List<Booking> {
        if (_currentAccount.value == null) {
            return listOf()
        }
        else {
            if (forceRefresh || !_currentSession.value!!.bookings.containsKey(date)) {
                try {
                    if (!_currentAccount.value!!.loggedIn)
                        _currentAccount.value!!.login()

                    _currentSession.value!!.bookings =
                        _currentAccount.value!!.getBookingsForWeek(LocalDate.parse(date).isoWeekNumber())
                } catch (e: Exception) {
                    _currentSession.value!!.bookings = HashMap()
                }
            }

            return _currentSession.value!!.bookings[date] ?: listOf()
        }
    }

    suspend fun toggleBookChoice(date: String, id: String, book: Boolean): List<Booking> {
        if (_currentAccount.value == null || !_currentSession.value!!.bookings.containsKey(date)) {
            return listOf()
        }

        try {
            this._currentAccount.value!!.toggleBookChoice(LocalDate.parse(date), id, book)
        } catch (_: Exception) {}

        return fetchBookings(date, forceRefresh = true)
    }
}
