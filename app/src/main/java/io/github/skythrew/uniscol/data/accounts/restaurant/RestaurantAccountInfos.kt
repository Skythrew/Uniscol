package io.github.skythrew.uniscol.data.accounts.restaurant

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import io.github.skythrew.uniscol.data.accounts.Account

enum class RestaurantAccountFeature {
    QRCode,
    Menu,
    Booking
}

@Entity(
    tableName = "restaurant_account_infos", foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RestaurantAccountInfos(
    @PrimaryKey
    val accountId: Int,
    val cardNumber: String? = null,
    val features: List<RestaurantAccountFeature>
)

