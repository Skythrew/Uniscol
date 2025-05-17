package io.github.skythrew.uniscol.data.accounts.restaurant

data class BookingChoice(
    val id: String,
    val label: String?,
    val quantity: Int?,
    val maxQuantity: Int?,
    val enabled: Boolean,
    val booked: Boolean
)
