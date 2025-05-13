package io.github.skythrew.uniscol.data.accounts

import io.github.skythrew.uniscol.data.services.ServiceType
import io.github.skythrew.uniscol.data.services.Services

enum class AccountFeature {
    RESTAURANT
}

data class AccountWithFeatures (
    override val id: Int,
    override val service: Services,
    override val type: ServiceType,
    override val label: String?,
    override val username: String?,
    override val password: String?,
    override val accessToken: String?,
    override val accessTokenExpiration: Long?,
    override val refreshToken: String?,
    override val refreshTokenExpiration: Long?,
    val features: List<AccountFeature>
) : AccountInterface