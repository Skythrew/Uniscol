package io.github.skythrew.uniscol.data.navigation

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TabRepository @Inject constructor(private val tabDao: TabDao) {

    suspend fun enableTab(tab: Tab) = tabDao.enable(tab.id)
    suspend fun disableTab(tab: Tab) = tabDao.disable(tab.id)

    suspend fun enableDestination(destination: String) = tabDao.enableByDestination(destination)
    suspend fun disableDestination(destination: String) = tabDao.disableByDestination(destination)

    suspend fun update(tab: Tab) = tabDao.update(tab)

    fun getAllTabsStream(): Flow<List<Tab>> {
        return tabDao.getAllTabs()
    }
}