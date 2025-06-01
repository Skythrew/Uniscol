package io.github.skythrew.uniscol.presentation.mailbox

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.skythrew.uniscol.data.accounts.conversation.Attachment
import io.github.skythrew.uniscol.data.accounts.conversation.ConversationSessionManager
import io.github.skythrew.uniscol.data.accounts.conversation.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    val mailboxState: MailboxState,
    private val conversationSessionManager: ConversationSessionManager
) : ViewModel() {
    val account = conversationSessionManager.currentAccount.value

    private val _message: MutableStateFlow<Message?> = MutableStateFlow(null)
    val message: StateFlow<Message?> = _message

    private val _attachments: MutableStateFlow<List<Attachment>> = MutableStateFlow(listOf())
    val attachments: StateFlow<List<Attachment>> = _attachments

    private val _selectedAttachment: MutableStateFlow<Attachment?> = MutableStateFlow(null)
    val selectedAttachment: StateFlow<Attachment?> = _selectedAttachment

    private val _downloadingAttachments: MutableStateFlow<Set<String>> = MutableStateFlow(setOf())
    val downloadingAttachments: StateFlow<Set<String>> = _downloadingAttachments

    suspend fun fetchMessage(id: String) {
        _message.value = conversationSessionManager.fetchMessage(id)
        _attachments.value = conversationSessionManager.getAttachments(_message.value!!)
    }

    suspend fun deleteMessage() {
        conversationSessionManager.deleteMessage(message.value!!)
    }

    fun toggleUnread() {
        viewModelScope.launch {
            _message.value = conversationSessionManager.toggleUnread(message.value!!)
        }
    }

    fun writeAttachment(uri: android.net.Uri) {
        viewModelScope.launch {
            val tempSet = _downloadingAttachments.value.toMutableSet()
            tempSet.add(_selectedAttachment.value!!.id)
            _downloadingAttachments.value = tempSet

            val attachment = _selectedAttachment.value!!.copy()

            _selectedAttachment.value = null

            context.contentResolver.openOutputStream(uri).use { outputStream ->
                if (outputStream != null) {
                    outputStream.write(conversationSessionManager.getAttachmentContent(attachment))
                    outputStream.close()
                } else {
                    Log.e("ERR", "Output stream unavailable ?")
                }

                val tempSet = _downloadingAttachments.value.toMutableSet()
                tempSet.remove(attachment.id)
                _downloadingAttachments.value = tempSet
            }
        }
    }

    fun selectAttachment(attachment: Attachment?) {
        _selectedAttachment.value = attachment
    }
}