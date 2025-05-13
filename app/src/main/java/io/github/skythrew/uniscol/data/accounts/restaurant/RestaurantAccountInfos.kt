package io.github.skythrew.uniscol.data.accounts.restaurant

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

enum class RestaurantAccountFeature {
    QRCode,
    Menu,
    Booking
}

@Entity(tableName = "restaurant_account_infos")
data class RestaurantAccountInfos(
    @PrimaryKey
    val accountId: Int,
    val cardNumber: String? = null,
    val features: List<RestaurantAccountFeature>
)

