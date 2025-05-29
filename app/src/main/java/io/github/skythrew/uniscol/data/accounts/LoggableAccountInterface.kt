package io.github.skythrew.uniscol.data.accounts

interface LoggableAccountInterface : AccountInterface {
    val client: Any
    suspend fun login()
}