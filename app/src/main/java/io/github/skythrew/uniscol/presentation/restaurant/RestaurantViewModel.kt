package io.github.skythrew.uniscol.presentation.restaurant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.skythrew.uniscol.data.accounts.restaurant.Booking
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountInterface
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountRepository
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantSessionManager
import io.github.skythrew.uniscol.data.dates.UniscolRawDateFormat
import io.github.skythrew.uniscol.data.network.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import javax.inject.Inject
import kotlinx.coroutines.flow.first as first1

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    val networkRepository: NetworkRepository,
    private val sessionManager: RestaurantSessionManager,
    accountsRepository: RestaurantAccountRepository
): ViewModel() {
    val accounts = accountsRepository.getAllAccountsStream()

    val selectedAccount = sessionManager.currentAccount
    val session = sessionManager.currentSession

    private val _cardNumber = MutableStateFlow<String?>(null)
    val cardNumber: StateFlow<String?> = _cardNumber

    private val _bookings = MutableStateFlow<List<Booking>>(listOf())
    val bookings: StateFlow<List<Booking>> = _bookings

    private val _bookingsDate = sessionManager.currentBookingsDate
    val bookingsDate: StateFlow<String> = _bookingsDate

    init {
        viewModelScope.launch {
            if (selectedAccount.value == null)
                sessionManager.setCurrentAccount(accounts.first1()[0])

            _cardNumber.value = sessionManager.currentAccount.value!!.cardNumber

            selectedAccount.collect { account ->
                if (account != null) {
                    bookingsDate.collect { date ->
                        _bookings.value = sessionManager.fetchBookings(date)
                    }
                }
            }
        }
    }

    fun updateSelectedAccount(account: RestaurantAccountInterface) {
        viewModelScope.launch {
            sessionManager.setCurrentAccount(account)
        }
    }

    fun updateBookingsDate(date: Instant) {
        _bookingsDate.value = date.format(UniscolRawDateFormat)
    }

    fun previousBookingsDate() {
        val currentDate = LocalDate.parse(_bookingsDate.value)
        _bookingsDate.value = currentDate.minus(1, DateTimeUnit.DAY).toString()
    }

    fun nextBookingsDate() {
        val currentDate = LocalDate.parse(_bookingsDate.value)
        _bookingsDate.value = currentDate.plus(1, DateTimeUnit.DAY).toString()
    }

    fun toggleBookingChoice(id: String, book: Boolean) {
        viewModelScope.launch {
            _bookings.value = sessionManager.toggleBookChoice(_bookingsDate.value, id, book)
        }
    }
}