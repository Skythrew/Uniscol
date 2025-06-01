package io.github.skythrew.uniscol.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.skythrew.uniscol.presentation.components.ButtonWithIcon
import io.github.skythrew.uniscol.presentation.components.TopAppBarNavigation
import io.github.skythrew.uniscol.presentation.components.UniscolTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, drawerState: DrawerState, navigateToSettings: () -> Unit) {
    Scaffold (
        topBar = { UniscolTopAppBar("Accueil", TopAppBarNavigation.Sidebar, drawerState, navController) }
    )

    { padding ->
        Column (
            modifier = Modifier.padding(padding).fillMaxWidth().fillMaxHeight().background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Bienvenue sur Uniscol",
                    textAlign = TextAlign.Center
                )
                Text(
                    "Ajoutez votre premier service de vie scolaire depuis les paramètres.",
                    textAlign = TextAlign.Center
                )

                ButtonWithIcon (
                    label = "Paramètres",
                    icon = Icons.Default.Settings
                ) {
                    navigateToSettings()
                }
            }
        }
    }

}