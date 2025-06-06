package io.github.skythrew.uniscol.data.accounts

import io.github.skythrew.uniscol.data.services.Services

interface AccountInterface {
    val id: Int
    val service: Services
    val label: String?
    val username: String?
    val password: String?
    val accessToken: String?
    val accessTokenExpiration: Long?
    val refreshToken: String?
    val refreshTokenExpiration: Long?
    val clientId: String?
    val clientSecret: String?
    val instance: String?
    val supportCanteen: Boolean
    val supportConversation: Boolean
}