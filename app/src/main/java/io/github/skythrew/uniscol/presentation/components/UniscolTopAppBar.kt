package io.github.skythrew.uniscol.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.coroutines.launch

enum class TopAppBarNavigation {
    Sidebar,
    Back
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniscolTopAppBar(
    title: String,
    navigation: TopAppBarNavigation,
    drawerState: DrawerState,
    navController: NavController,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    actions: @Composable RowScope.() -> Unit = {}
) {
    var backButtonEnabled by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        colors = colors,
        title = {
            Text(title)
        },
        modifier = modifier.fillMaxWidth(),
        navigationIcon = {
            when (navigation) {
                TopAppBarNavigation.Sidebar -> {
                    IconButton(
                        onClick = {
                            coroutineScope.launch { drawerState.open() }
                        }
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }

                TopAppBarNavigation.Back -> {
                    IconButton(
                        onClick = {
                            backButtonEnabled = false
                            navController.popBackStack()
                        },
                        enabled = backButtonEnabled
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            }

        },

        actions = actions
    )
}