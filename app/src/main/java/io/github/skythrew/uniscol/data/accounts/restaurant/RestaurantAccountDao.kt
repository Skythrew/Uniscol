package io.github.skythrew.uniscol.data.accounts.restaurant

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.github.skythrew.uniscol.data.accounts.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantAccountDao {
    @Query("SELECT * FROM accounts JOIN restaurant_account_infos ON restaurant_account_infos.accountId = accounts.id WHERE id = :id")
    fun getAccountById(id: Int): Flow<RestaurantAccountWithInfos>

    @Query("SELECT * FROM accounts JOIN restaurant_account_infos ON restaurant_account_infos.accountId = accounts.id WHERE supportCanteen = 1")
    fun getAllAccounts(): Flow<List<RestaurantAccountWithInfos>>

    @Insert
    suspend fun insertAccount(account: Account): Long

    @Insert
    suspend fun insertInfos(infos: RestaurantAccountInfos)

    @Transaction
    suspend fun insertAccountWithInfos(account: Account, infos: RestaurantAccountInfos) {
        val accountId = insertAccount(account)
        insertInfos(infos.copy(accountId = accountId.toInt()))
    }
}