package io.github.skythrew.uniscol.data.accounts

interface LoggableAccountInterface : AccountInterface {
    val client: Any
    var loggedIn: Boolean
    suspend fun login()
}