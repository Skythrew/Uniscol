package io.github.skythrew.uniscol.data.accounts.conversation

import androidx.room.Dao
import androidx.room.Query
import io.github.skythrew.uniscol.data.accounts.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationAccountDao {
    @Query("SELECT * FROM accounts WHERE supportConversation = 1")
    fun getAllAccounts(): Flow<List<Account>>
}