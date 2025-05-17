package io.github.skythrew.uniscol.data.accounts.restaurant

import io.github.skythrew.uniscol.data.accounts.LoggableAccountInterface
import kotlinx.datetime.LocalDate

interface RestaurantAccountInterface : LoggableAccountInterface {
    val cardNumber: String?
    val features: Set<RestaurantAccountFeature>
    suspend fun getBalance(): Int?
    suspend fun getBookingsForWeek(week: Int): HashMap<String, List<Booking>>
    suspend fun toggleBookChoice(date: LocalDate, id: String, book: Boolean): Boolean
}