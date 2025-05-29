package io.github.skythrew.uniscol.data.network

import io.github.skythrew.uniscol.data.accounts.LoggableAccountInterface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationRepository @Inject constructor() {
    private val _accounts: MutableMap<Int, Pair<Any, Boolean>> = mutableMapOf()
    val accounts: Map<Int, Pair<Any, Boolean>> = _accounts

    fun setAccount(accountId: Int, client: Any): Pair<Any, Boolean> {
        _accounts[accountId] = Pair(client, false)
        return _accounts[accountId]!!
    }

    fun getAccountClient(accountId: Int) = _accounts[accountId]?.first
    fun getAccountLoginState(accountId: Int) = _accounts[accountId]?.second

    suspend fun login(account: LoggableAccountInterface, force: Boolean = false) {
        if (getAccountLoginState(account.id) == true)
            return

        account.login()

        _accounts[account.id] = Pair(_accounts[account.id]!!.first, true)
    }
}