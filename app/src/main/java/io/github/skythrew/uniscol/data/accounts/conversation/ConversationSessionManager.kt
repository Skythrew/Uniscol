package io.github.skythrew.uniscol.data.accounts.conversation

import android.util.Log
import io.github.skythrew.uniscol.data.network.AuthenticationRepository
import io.github.skythrew.uniscol.data.network.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class ConversationSession(
    val account: ConversationAccountInterface,
    var messages: Map<String, Message> = mapOf()
)

@Singleton
class ConversationSessionManager @Inject constructor(
    val networkRepository: NetworkRepository,
    val authenticationRepository: AuthenticationRepository
) {
    val _currentAccount = MutableStateFlow<ConversationAccountInterface?>(null)
    val currentAccount: StateFlow<ConversationAccountInterface?> = _currentAccount

    val _currentSession = MutableStateFlow<ConversationSession?>(null)
    val currentSession: StateFlow<ConversationSession?> = _currentSession

    val _currentMessages: MutableStateFlow<List<Message>> = MutableStateFlow(listOf())
    val currentMessages: StateFlow<List<Message>> = _currentMessages

    private val authenticatedAccounts = HashMap<Int, ConversationSession>()

    fun updateCurrentMessages() {
        _currentMessages.value = _currentSession.value!!.messages.values.toList()
    }
    fun setCurrentAccount(account: ConversationAccountInterface) {
        if (!authenticatedAccounts.containsKey(account.id)) {
            authenticatedAccounts[account.id] = ConversationSession(account)
        }

        _currentSession.value = authenticatedAccounts[account.id]!!
        updateCurrentMessages()
        _currentAccount.value = authenticatedAccounts[account.id]!!.account
    }


    suspend fun fetchMessages() {
        if (_currentAccount.value == null) {
            return
        }
        else {
            if (_currentMessages.value.isEmpty())
                try {
                    authenticationRepository.login(_currentAccount.value!!)

                    val tempMap = mutableMapOf<String, Message>()

                    _currentAccount.value!!.getBaseFolderMessages().forEach { message ->
                        tempMap[message.id] = message
                    }

                    _currentSession.value!!.messages = tempMap
                    updateCurrentMessages()
                } catch (e: Exception) {
                    Log.e("ERR", e.stackTraceToString())
                    _currentSession.value!!.messages = mapOf()
                    updateCurrentMessages()
                }
        }
    }

    suspend fun fetchMessage(id: String): Message? {
        if (_currentAccount.value == null) {
            return null
        }
        else {
            if (_currentSession.value!!.messages[id]?.fetched == true)
                return _currentSession.value!!.messages[id]

            val message = _currentAccount.value!!.fetchMessage(_currentSession.value!!, id)

            val tempMap = _currentSession.value!!.messages.toMutableMap()

            tempMap[id] = message.copy(fetched = true)

            _currentSession.value!!.messages = tempMap

            updateCurrentMessages()

            return tempMap[id]
        }
    }

    suspend fun deleteMessage(message: Message) {
        _currentAccount.value!!.deleteMessage(message)

        val tempMap = _currentSession.value!!.messages.toMutableMap()

        tempMap.remove(message.id)

        _currentSession.value!!.messages = tempMap

        updateCurrentMessages()
    }

    suspend fun toggleUnread(message: Message): Message? {
        if (!_currentAccount.value!!.supportConversationToggleUnread)
            return null

        _currentAccount.value!!.toggleUnread(message, !message.unread)

        val tempMap = _currentSession.value!!.messages.toMutableMap()

        tempMap[message.id] = message.copy(unread = !message.unread)

        _currentSession.value!!.messages = tempMap

        updateCurrentMessages()

        return tempMap[message.id]
    }

    suspend fun getAttachments(message: Message): List<Attachment> {
        return currentAccount.value!!.getAttachments(message)
    }

    suspend fun getAttachmentContent(attachment: Attachment): ByteArray {
        return currentAccount.value!!.getAttachmentContent(attachment)
    }
}
