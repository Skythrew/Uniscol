package io.github.skythrew.uniscol.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.skythrew.uniscol.data.accounts.Account
import io.github.skythrew.uniscol.data.accounts.AccountDao
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountDao
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountInfos
import io.github.skythrew.uniscol.data.navigation.Routes
import io.github.skythrew.uniscol.data.navigation.Tab
import io.github.skythrew.uniscol.data.navigation.TabDao
import io.github.skythrew.uniscol.data.navigation.TabIcon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DatabaseInitCallback(private val context: Context) : RoomDatabase.Callback() {
    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)

        CoroutineScope(Dispatchers.IO).launch {
            val tabDao = UniscolDatabase.getDatabase(context).tabDao()

            val tabs = listOf(
                Tab(
                    id = 0,
                    name = "Accueil",
                    icon = TabIcon.HOME,
                    iconSelected = TabIcon.HOME_SELECTED,
                    enabled = true,
                    destination = Routes.Home,
                    position = 0
                ),
                Tab(
                    id = 1,
                    name = "Paramètres",
                    icon = TabIcon.SETTINGS,
                    iconSelected = TabIcon.SETTINGS_SELECTED,
                    enabled = true,
                    destination = Routes.Settings,
                    position = 9999
                ),
                Tab(
                    id = 2,
                    name = "Cantine",
                    icon = TabIcon.RESTAURANT,
                    iconSelected = TabIcon.RESTAURANT_SELECTED,
                    enabled = false,
                    destination = Routes.Restaurant,
                    position = 1
                )
            )

            val storedTabs = tabDao.getAllTabs().first()

            tabs.forEach { tab ->
                if (tab.id > storedTabs.count() - 1)
                    tabDao.insert(tab)
            }
        }
    }
}

@Database(entities = [Account::class, RestaurantAccountInfos::class, Tab::class], version = 7, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UniscolDatabase: RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun restaurantAccountDao(): RestaurantAccountDao
    abstract fun tabDao(): TabDao

    companion object {
        @Volatile
        private var Instance: UniscolDatabase? = null

        fun getDatabase(context: Context): UniscolDatabase {
            return Instance ?: synchronized(this) {
                Room
                    .databaseBuilder(context, UniscolDatabase::class.java, "uniscol_database")
                    .addCallback(DatabaseInitCallback(context))
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}