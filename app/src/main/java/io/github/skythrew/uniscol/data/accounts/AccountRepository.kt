package io.github.skythrew.uniscol.data.accounts

import io.github.skythrew.turboselfkt.core.TurboselfClient
import io.github.skythrew.uniscol.data.services.Services
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor (private val accountDao: AccountDao) {
    suspend fun insertAccount(account: Account): Long = accountDao.insert(account)

    suspend fun updateAccount(account: Account) = accountDao.update(account)

    suspend fun deleteAccount(account: Account) = accountDao.delete(account)

    fun getAllAccountsStream(): Flow<List<Account>> {
        return accountDao.getAllAccounts()
    }
}