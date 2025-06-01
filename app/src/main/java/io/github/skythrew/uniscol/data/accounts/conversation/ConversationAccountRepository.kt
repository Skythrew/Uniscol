package io.github.skythrew.uniscol.data.accounts.conversation

import io.github.skythrew.edificekt.EdificeClient
import io.github.skythrew.uniscol.data.accounts.Account
import io.github.skythrew.uniscol.data.accounts.edifice.EdificeAccount
import io.github.skythrew.uniscol.data.network.AuthenticationRepository
import io.github.skythrew.uniscol.data.services.Services
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConversationAccountRepository @Inject constructor(
    val authenticationRepository: AuthenticationRepository,
    val accountDao: ConversationAccountDao
) {
    private fun mapAccountToConversation(account: Account): ConversationAccountInterface {
        when (account.service) {
            Services.Edifice -> {
                var accountClient = authenticationRepository.getAccountClient(account.id)

                if (accountClient == null) {
                    accountClient = authenticationRepository.setAccount(
                        account.id,
                        EdificeClient(account.instance!!)
                    ).first
                }

                return EdificeAccount(
                    id = account.id,
                    client = accountClient as EdificeClient,
                    service = account.service,
                    label = account.label,
                    username = account.username,
                    password = account.password,
                    accessToken = account.accessToken,
                    accessTokenExpiration = account.accessTokenExpiration,
                    refreshToken = account.refreshToken,
                    refreshTokenExpiration = account.refreshTokenExpiration,
                    supportCanteen = account.supportCanteen,
                    supportConversation = account.supportConversation,
                    clientId = account.clientId,
                    clientSecret = account.clientSecret,
                    instance = account.instance,
                    originalAccount = account
                )
            }

            else -> error("Unsupported service")
        }
    }

    fun getAllAccountsStream(): Flow<List<ConversationAccountInterface>> {
        return accountDao.getAllAccounts().map { accounts ->
            accounts.map { mapAccountToConversation(it) }
        }
    }
}