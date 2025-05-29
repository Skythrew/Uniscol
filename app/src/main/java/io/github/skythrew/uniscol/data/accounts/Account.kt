package io.github.skythrew.uniscol.data.accounts

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.skythrew.uniscol.data.services.ServiceType
import io.github.skythrew.uniscol.data.services.Services

@Entity(tableName = "accounts")
open class Account(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,
    override val service: Services,
    override val label: String? = null,
    override val username: String? = null,
    override val password: String? = null,
    override val accessToken: String? = null,
    override val accessTokenExpiration: Long? = null,
    override val refreshToken: String? = null,
    override val refreshTokenExpiration: Long? = null,
    override val supportCanteen: Boolean,
    override val supportConversation: Boolean
) : AccountInterface
