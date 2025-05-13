package io.github.skythrew.uniscol.data.accounts.restaurant

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantAccountDao {
    @Query("SELECT * FROM accounts JOIN restaurant_account_infos ON restaurant_account_infos.accountId = accounts.id WHERE id = :id")
    fun getAccountById(id: Int): Flow<RestaurantAccountWithInfos>

    @Query("SELECT * FROM accounts JOIN restaurant_account_infos ON restaurant_account_infos.accountId = accounts.id WHERE type = 'CANTEEN'")
    fun getAllAccounts(): Flow<List<RestaurantAccountWithInfos>>

    @Insert
    fun insertInfos(infos: RestaurantAccountInfos)
}