package io.github.skythrew.uniscol.presentation.settings.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.github.skythrew.uniscol.presentation.components.TopAppBarNavigation
import io.github.skythrew.uniscol.presentation.components.UniscolTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterfaceSettingsScreen(navController: NavController, drawerState: DrawerState) {
    val appViewModel: InterfaceSettingsViewModel = hiltViewModel()

    val tabs by appViewModel.tabs.collectAsState(listOf())

    Scaffold (
        topBar = {
            UniscolTopAppBar(
                title = "Interface",
                navigation = TopAppBarNavigation.Back,
                drawerState = drawerState,
                navController = navController
            )
        }
    ) { padding ->
        Column (
            modifier = Modifier.padding(padding).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Onglets",
                style = MaterialTheme.typography.labelLarge
            )

                Box (
                    modifier = Modifier.border(BorderStroke(1.dp, Color.LightGray), shape = RoundedCornerShape(20.dp))
                ) {
                    LazyColumn (
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(items = tabs, key = {_, tab -> tab.id}) {index, tab ->
                            Row (
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.animateItem(
                                    fadeInSpec = null,
                                    fadeOutSpec = null
                                ).fillMaxWidth().padding(bottom = if (index < tabs.count() - 1) 8.dp else 0.dp)
                            ) {
                                Row (
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    IconButton(onClick = {}) {
                                        if (tab.icon == null)
                                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                                        else
                                            Icon(imageVector = tab.icon.imageVector(), contentDescription = "")
                                    }

                                    Text(tab.name, style = MaterialTheme.typography.labelMedium)
                                }

                                Row (
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    IconButton(onClick = {
                                        appViewModel.makeTabPositionGoUp(index)
                                    }, enabled = index > 0) {
                                        Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "")
                                    }
                                    IconButton(onClick = {
                                        appViewModel.makeTabPositionGoDown(index)
                                    }, enabled = index < tabs.count() - 1) {
                                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
                                    }
                                }

                            }

                            if (index < tabs.count() - 1)
                                HorizontalDivider()
                        }
                    }
                }

        }
    }
}