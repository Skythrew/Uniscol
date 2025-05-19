package io.github.skythrew.uniscol.data.navigation

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TabDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tab: Tab): Long

    @Update
    suspend fun update(tab: Tab)

    @Delete
    suspend fun delete(tab: Tab)

    @Query("UPDATE tabs SET enabled = 1 WHERE id = :id")
    suspend fun enable(id: Int)

    @Query("UPDATE tabs SET enabled = 0 WHERE id = :id")
    suspend fun disable(id: Int)

    @Query("UPDATE tabs SET enabled = 1 WHERE destination = :destination")
    suspend fun enableByDestination(destination: String)

    @Query("UPDATE tabs SET enabled = 0 WHERE destination = :destination")
    suspend fun disableByDestination(destination: String)

    @Query("SELECT * FROM tabs WHERE enabled = 1 ORDER by position")
    fun getAllTabs(): Flow<List<Tab>>
}