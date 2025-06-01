package io.github.skythrew.uniscol.data.accounts.edifice

import io.github.skythrew.edificekt.EdificeClient
import io.github.skythrew.edificekt.models.conversation.MessageAttachment
import io.github.skythrew.uniscol.data.accounts.Account
import io.github.skythrew.uniscol.data.accounts.Tokens
import io.github.skythrew.uniscol.data.accounts.conversation.Attachment
import io.github.skythrew.uniscol.data.accounts.conversation.ConversationAccountInterface
import io.github.skythrew.uniscol.data.accounts.conversation.ConversationSession
import io.github.skythrew.uniscol.data.accounts.conversation.Message
import io.github.skythrew.uniscol.data.services.Services

data class EdificeAccount(
    override val id: Int,
    override val client: EdificeClient,
    override val service: Services,
    override val label: String?,
    override val username: String?,
    override val password: String?,
    override val accessToken: String?,
    override val accessTokenExpiration: Long?,
    override val refreshToken: String?,
    override val refreshTokenExpiration: Long?,
    override val supportCanteen: Boolean,
    override val supportConversation: Boolean,
    override val clientId: String?,
    override val clientSecret: String?,
    override val instance: String?,
    override val originalAccount: Account,
    override val supportConversationToggleUnread: Boolean = true
) : ConversationAccountInterface {
    private fun mapToMessage(message: io.github.skythrew.edificekt.models.conversation.Message): Message {
        return Message(
            id = message.id,
            author = message.fromName.toString(),
            authorPictureUrl = instance + "/userbook/avatar/" + message.from,
            authorId = message.from,
            subject = message.subject ?: "Aucun objet",
            body = message.body ?: "",
            unread = message.unread,
            hasAttachments = message.hasAttachment == true,
            attachments = message.attachments?.map { attachment ->
                Attachment(
                    id = attachment.id,
                    name = attachment.filename,
                    size = attachment.size,
                    orig = attachment
                )
            } ?: listOf(),
            orig = message
        )
    }

    override suspend fun getBaseFolderMessages(): List<Message> {
        val messages = client.conversations.getFolderMessages("inbox")

        return messages.map {
            mapToMessage(it)
        }
    }

    override suspend fun login() {
        client.loginByOauth2Token(clientId!!, clientSecret!!, accessToken!!, refreshToken!!)
    }

    override fun tokens(): Tokens? {
        if (client.accessToken == null)
            return null

        return Tokens(client.accessToken!!, client.refreshToken!!)
    }

    override suspend fun fetchMessage(session: ConversationSession, id: String): Message {
        return mapToMessage(client.conversations.getMessage(id))
    }

    override suspend fun deleteMessage(message: Message) {
        client.conversations.moveToTrash(listOf(message.orig as io.github.skythrew.edificekt.models.conversation.Message))
    }

    override suspend fun toggleUnread(message: Message, unread: Boolean) {
        client.conversations.setMessagesReadStatus(
            listOf(message.orig as io.github.skythrew.edificekt.models.conversation.Message),
            read = !unread
        )
    }

    override suspend fun getAttachments(message: Message): List<Attachment> {
        return message.attachments
    }

    override suspend fun getAttachmentContent(attachment: Attachment): ByteArray {
        return client.conversations.getMessageAttachment(attachment.orig as MessageAttachment)
    }
}
