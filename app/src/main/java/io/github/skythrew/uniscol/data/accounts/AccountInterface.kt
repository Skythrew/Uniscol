package io.github.skythrew.uniscol.data.accounts

import io.github.skythrew.uniscol.data.services.ServiceType
import io.github.skythrew.uniscol.data.services.Services

interface AccountInterface {
    val id: Int
    val service: Services
    val type: ServiceType
    val label: String?
    val username: String?
    val password: String?
    val accessToken: String?
    val accessTokenExpiration: Long?
    val refreshToken: String?
    val refreshTokenExpiration: Long?
}