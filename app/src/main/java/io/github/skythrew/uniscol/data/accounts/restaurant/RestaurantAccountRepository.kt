package io.github.skythrew.uniscol.data.accounts.restaurant

import io.github.skythrew.turboselfkt.core.TurboselfClient
import io.github.skythrew.uniscol.data.accounts.Account
import io.github.skythrew.uniscol.data.accounts.AccountDao
import io.github.skythrew.uniscol.data.accounts.turboself.TurboselfAccount
import io.github.skythrew.uniscol.data.navigation.Routes
import io.github.skythrew.uniscol.data.navigation.TabDao
import io.github.skythrew.uniscol.data.network.AuthenticationRepository
import io.github.skythrew.uniscol.data.services.Services
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantAccountRepository @Inject constructor (
    private val authenticationRepository: AuthenticationRepository,
    private val accountDao: AccountDao,
    private val restaurantAccountDao: RestaurantAccountDao,
    private val tabsDao: TabDao
) {
    private fun mapAccountToRestaurant(account: RestaurantAccountWithInfos): RestaurantAccountInterface =
        when (account.account.service) {
            Services.Turboself -> {
                var accountClient = authenticationRepository.getAccountClient(account.account.id)

                if (accountClient == null) {
                    accountClient = authenticationRepository.setAccount(account.account.id, TurboselfClient()).first
                }

                TurboselfAccount(
                    account.account.id,
                    client = accountClient as TurboselfClient,
                    service = account.account.service,
                    label = account.account.label,
                    username = account.account.username!!,
                    password = account.account.password!!,
                    accessToken = account.account.accessToken,
                    accessTokenExpiration = account.account.accessTokenExpiration,
                    refreshToken = account.account.refreshToken,
                    refreshTokenExpiration = account.account.refreshTokenExpiration,
                    cardNumber = account.infos.cardNumber,
                    features = account.infos.features.toSet(),
                    supportCanteen = account.account.supportCanteen,
                    supportConversation = account.account.supportConversation
                )
            }
        }

    fun getAllAccountsStream(): Flow<List<RestaurantAccountInterface>> {
        return restaurantAccountDao.getAllAccounts().map { accounts ->
            accounts.map { mapAccountToRestaurant(it) }
        }
    }

    suspend fun insertAccount(account: Account, infos: RestaurantAccountInfos) {
        restaurantAccountDao.insertAccountWithInfos(account, infos)
        tabsDao.enableByDestination(Routes.Restaurant)
    }
}