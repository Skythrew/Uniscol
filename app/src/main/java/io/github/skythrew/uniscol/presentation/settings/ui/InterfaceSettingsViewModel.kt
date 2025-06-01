package io.github.skythrew.uniscol.presentation.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.skythrew.uniscol.data.navigation.TabRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterfaceSettingsViewModel @Inject constructor(
    private val tabsRepository: TabRepository
) : ViewModel() {
    val tabs = tabsRepository.getAllTabsStream()

    fun makeTabPositionGoUp(index: Int) {
        viewModelScope.launch {
            val currentTabs = tabs.first()
            if (index != 0) {
                val prevTab = currentTabs[index - 1]
                tabsRepository.update(currentTabs[index].copy(position = index - 1))
                tabsRepository.update(prevTab.copy(position = index))
            }
        }
    }

    fun makeTabPositionGoDown(index: Int) {
        viewModelScope.launch {
            val currentTabs = tabs.first()
            if (index < currentTabs.count() - 1) {
                val nextTab = currentTabs[index + 1]
                tabsRepository.update(nextTab.copy(position = index))
                tabsRepository.update(currentTabs[index].copy(position = index + 1))
            }
        }
    }
}