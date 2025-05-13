package io.github.skythrew.uniscol.data.accounts.restaurant

import io.github.skythrew.uniscol.data.accounts.LoggableAccountInterface

interface RestaurantAccountInterface : LoggableAccountInterface {
    val cardNumber: String?
    val features: Set<RestaurantAccountFeature>
    suspend fun getBalance(): Int?
}