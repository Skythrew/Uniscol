package io.github.skythrew.uniscol.data.accounts.turboself

import io.github.skythrew.turboselfkt.core.TurboselfClient
import io.github.skythrew.uniscol.data.accounts.Account
import io.github.skythrew.uniscol.data.accounts.Tokens
import io.github.skythrew.uniscol.data.accounts.restaurant.Booking
import io.github.skythrew.uniscol.data.accounts.restaurant.BookingChoice
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountFeature
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountInterface
import io.github.skythrew.uniscol.data.dates.UniscolRawDateFormat
import io.github.skythrew.uniscol.data.services.Services
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format

data class TurboselfAccount(
    override val id: Int,
    override val client: TurboselfClient,
    override val label: String?,
    override val cardNumber: String? = null,
    override val service: Services,
    override val username: String,
    override val password: String,
    override val accessToken: String?,
    override val accessTokenExpiration: Long?,
    override val refreshToken: String?,
    override val refreshTokenExpiration: Long?,
    override val features: Set<RestaurantAccountFeature>,
    override val supportCanteen: Boolean,
    override val supportConversation: Boolean,
    override val clientId: String?,
    override val clientSecret: String?,
    override val originalAccount: Account,
    override val instance: String? = null
) : RestaurantAccountInterface {
    override suspend fun login() {
        this.client.loginWithCredentials(this.username, this.password)
    }

    override suspend fun getBalance(): Int = this.client.balances()[0].amount

    override suspend fun getBookingsForWeek(week: Int): HashMap<String, List<Booking>> {
        val bookings = this.client.bookings(week)

        val choices: HashMap<String, MutableList<BookingChoice>> = HashMap()

        bookings.forEach { booking ->
            booking.days.forEach { bookingDay ->
                val choice = BookingChoice(
                    id = booking.id,
                    quantity = bookingDay.reservations,
                    maxQuantity = null,
                    enabled = bookingDay.canBook,
                    label = booking.terminal.name,
                    booked = bookingDay.booked
                )

                val formattedDate = bookingDay.date.format(UniscolRawDateFormat)

                if (choices.containsKey(formattedDate)) {
                    choices[formattedDate]!!.add(choice)
                } else {
                    choices[formattedDate] = mutableListOf(choice)
                }
            }
        }

        val result: HashMap<String, List<Booking>> = HashMap()

        choices.forEach { (date, c) -> result[date] = listOf(Booking(choices = c)) }

        return result
    }

    override suspend fun toggleBookChoice(date: LocalDate, id: String, book: Boolean): Boolean {
        return this.client.bookMeal(
            id,
            date.dayOfWeek.value.toShort(),
            reservations = if (book) 1 else 0
        ).booked
    }

    override fun tokens(): Tokens? {
        return null
    }
}
