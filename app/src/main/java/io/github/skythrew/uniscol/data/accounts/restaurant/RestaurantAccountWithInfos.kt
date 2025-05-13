package io.github.skythrew.uniscol.data.accounts.restaurant

import androidx.room.Embedded
import androidx.room.Relation
import io.github.skythrew.uniscol.data.accounts.Account

data class RestaurantAccountWithInfos(
    @Embedded val account: Account,
    @Relation(
        parentColumn = "id",
        entityColumn = "accountId"
    )
    val infos: RestaurantAccountInfos
)
