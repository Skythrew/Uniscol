package io.github.skythrew.uniscol.data.accounts

data class Tokens(
    val accessToken: String,
    val refreshToken: String
)

interface LoggableAccountInterface : AccountInterface {
    val client: Any
    val originalAccount: Account
    suspend fun login()

    fun tokens(): Tokens?
}