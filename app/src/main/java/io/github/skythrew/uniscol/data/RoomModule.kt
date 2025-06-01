package io.github.skythrew.uniscol.data

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.skythrew.uniscol.data.accounts.AccountDao
import io.github.skythrew.uniscol.data.accounts.conversation.ConversationAccountDao
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountDao
import io.github.skythrew.uniscol.data.navigation.TabDao

@Module
@InstallIn(SingletonComponent::class)
object AccountModule {
    @Provides
    fun provideUniscolDatabase(@ApplicationContext context: Context): UniscolDatabase {
        return UniscolDatabase.getDatabase(context)
    }

    @Provides
    fun provideTabsDao(uniscolDatabase: UniscolDatabase): TabDao {
        return uniscolDatabase.tabDao()
    }

    @Provides
    fun provideAccountDao(uniscolDatabase: UniscolDatabase): AccountDao {
        return uniscolDatabase.accountDao()
    }

    @Provides
    fun provideRestaurantAccountDao(uniscolDatabase: UniscolDatabase): RestaurantAccountDao {
        return uniscolDatabase.restaurantAccountDao()
    }

    @Provides
    fun provideConversationAccountDao(uniscolDatabase: UniscolDatabase): ConversationAccountDao {
        return uniscolDatabase.conversationAccountDao()
    }
}