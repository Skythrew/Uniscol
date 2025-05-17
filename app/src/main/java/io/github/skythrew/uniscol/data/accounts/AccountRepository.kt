package io.github.skythrew.uniscol.data.accounts

import kotlinx.coroutines.flow.Flow
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