package io.github.skythrew.uniscol.data.accounts.restaurant.turboself

import io.github.skythrew.turboselfkt.core.TurboselfClient
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountFeature
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountInterface
import io.github.skythrew.uniscol.data.services.ServiceType
import io.github.skythrew.uniscol.data.services.Services

data class TurboselfAccount(
    override val id: Int,
    override val client: TurboselfClient,
    override var loggedIn: Boolean,
    override val label: String?,
    override val cardNumber: String? = null,
    override val service: Services,
    override val type: ServiceType,
    override val username: String,
    override val password: String,
    override val accessToken: String?,
    override val accessTokenExpiration: Long?,
    override val refreshToken: String?,
    override val refreshTokenExpiration: Long?,
    override val features: Set<RestaurantAccountFeature>
) : RestaurantAccountInterface {
    override suspend fun login() {
        this.client.loginWithCredentials(this.username, this.password)
        this.loggedIn = true
    }

    override suspend fun getBalance(): Int = this.client.balances()[0].amount
}
