package io.github.skythrew.uniscol.presentation.mailbox

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.skythrew.uniscol.data.accounts.conversation.ConversationAccountInterface
import io.github.skythrew.uniscol.data.accounts.conversation.ConversationAccountRepository
import io.github.skythrew.uniscol.data.accounts.conversation.ConversationSessionManager
import io.github.skythrew.uniscol.data.accounts.conversation.Message
import io.github.skythrew.uniscol.data.network.NetworkRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.first as first1

@HiltViewModel
class MailboxViewModel @Inject constructor(
    val networkRepository: NetworkRepository,
    mailboxState: MailboxState,
    private val sessionManager: ConversationSessionManager,
    accountsRepository: ConversationAccountRepository
): ViewModel() {
    val accounts = accountsRepository.getAllAccountsStream()

    val snackbarHostState = mailboxState.snackbarHostState

    val selectedAccount = sessionManager.currentAccount

    val lazyListState = LazyListState()

    val messages: StateFlow<List<Message>> = sessionManager.currentMessages

    init {
        viewModelScope.launch {
            selectedAccount.collect { account ->
                sessionManager.fetchMessages()
            }
        }
        viewModelScope.launch {
            if (selectedAccount.value == null)
                sessionManager.setCurrentAccount(accounts.first1()[0])
        }
    }

    fun updateSelectedAccount(account: ConversationAccountInterface) {
        viewModelScope.launch {
            sessionManager.setCurrentAccount(account)
            lazyListState.scrollToItem(0)
        }
    }
}