package io.github.skythrew.uniscol.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.skythrew.uniscol.data.accounts.Account
import io.github.skythrew.uniscol.data.accounts.AccountDao
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountDao
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountInfos

@Database(entities = [Account::class, RestaurantAccountInfos::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UniscolDatabase: RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun restaurantAccountDao(): RestaurantAccountDao

    companion object {
        @Volatile
        private var Instance: UniscolDatabase? = null

        fun getDatabase(context: Context): UniscolDatabase {
            return Instance ?: synchronized(this) {
                Room
                    .databaseBuilder(context, UniscolDatabase::class.java, "uniscol_database")
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}