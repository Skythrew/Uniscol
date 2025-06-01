package io.github.skythrew.uniscol.data.accounts.conversation

data class Attachment(
    val id: String,
    val name: String,
    val size: Long,
    val orig: Any
)

data class Message(
    val id: String,
    val author: String,
    val authorPictureUrl: String?,
    val authorId: String?,
    val subject: String,
    val body: String,
    val unread: Boolean,
    val fetched: Boolean = false,
    val hasAttachments: Boolean,
    val attachments: List<Attachment>,
    val orig: Any
)
