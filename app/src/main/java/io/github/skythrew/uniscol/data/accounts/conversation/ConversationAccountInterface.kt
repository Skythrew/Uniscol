package io.github.skythrew.uniscol.data.accounts.conversation

import io.github.skythrew.uniscol.data.accounts.LoggableAccountInterface

interface ConversationAccountInterface : LoggableAccountInterface {
    suspend fun getBaseFolderMessages(): List<Message>
    suspend fun fetchMessage(session: ConversationSession, id: String): Message
    suspend fun deleteMessage(message: Message)
    suspend fun toggleUnread(message: Message, unread: Boolean)
    suspend fun getAttachments(message: Message): List<Attachment>
    suspend fun getAttachmentContent(attachment: Attachment): ByteArray

    val supportConversationToggleUnread: Boolean
}