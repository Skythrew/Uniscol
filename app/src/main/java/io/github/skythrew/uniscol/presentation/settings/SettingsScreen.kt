package io.github.skythrew.uniscol.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.skythrew.uniscol.data.navigation.Routes
import io.github.skythrew.uniscol.presentation.components.TopAppBarNavigation
import io.github.skythrew.uniscol.presentation.components.UniscolTopAppBar
import io.github.skythrew.uniscol.presentation.settings.components.SettingsMenuGoButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, drawerState: DrawerState) {
    Scaffold(
        topBar = {
            UniscolTopAppBar(
                title = "Paramètres",
                navigation = TopAppBarNavigation.Sidebar,
                drawerState = drawerState,
                navController = navController
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxHeight()
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
            ) {
                SettingsMenuGoButton(icon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Comptes",
                        modifier = Modifier.size(32.dp)
                    )
                }, "Comptes", description = "Gérez vos différents comptes") {
                    navController.navigate(Routes.AccountSettings) { launchSingleTop = true }
                }

                SettingsMenuGoButton(icon = {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Interface",
                        modifier = Modifier.size(32.dp)
                    )
                }, "Interface", description = "Personnalisez l'interface ") {
                    navController.navigate(Routes.InterfaceSettings) { launchSingleTop = true }
                }
            }

        }
    }

}