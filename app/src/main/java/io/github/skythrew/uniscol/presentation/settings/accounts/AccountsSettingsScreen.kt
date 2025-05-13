package io.github.skythrew.uniscol.presentation.settings.accounts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.github.skythrew.uniscol.R
import io.github.skythrew.uniscol.data.services.ServiceRepository
import io.github.skythrew.uniscol.presentation.components.TopAppBarNavigation
import io.github.skythrew.uniscol.presentation.components.UniscolTopAppBar
import io.github.skythrew.uniscol.presentation.navigation.TurboselfLogin
import io.github.skythrew.uniscol.presentation.settings.components.SettingsMenuButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsSettingsScreen(navController: NavController, drawerState: DrawerState) {
    val viewModel: AccountsSettingsViewModel = hiltViewModel()
    val accounts by viewModel.accounts.collectAsState(listOf())
    var showServicesModalBottomSheet by remember { mutableStateOf(false) }
    val modalBottomSheetState = rememberModalBottomSheetState()

    val serviceRepository = ServiceRepository()

    Scaffold (
        topBar = {
            UniscolTopAppBar(
                title = "Paramètres des comptes",
                navigation = TopAppBarNavigation.Back,
                drawerState = drawerState,
                navController = navController
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showServicesModalBottomSheet = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Ajouter")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (accounts.isEmpty())

                Column(
                    modifier = Modifier.fillMaxHeight().fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Ajoutez un compte pour commencer.")
                }
            else
                Column (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    accounts.forEach { account ->
                        SettingsMenuButton(
                            icon = {
                                Image(
                                    painter = painterResource(serviceRepository.getIconForService(account.service)),
                                    contentDescription = account.service.name,
                                    modifier = Modifier.size(32.dp)
                                )
                            },
                            label = account.username ?: account.service.name,
                            labelStyle = MaterialTheme.typography.labelLarge,
                            description = null
                        ) { }
                    }
                }
        }

        if (showServicesModalBottomSheet)
            ModalBottomSheet(
                onDismissRequest = {
                    showServicesModalBottomSheet = false
                },
                sheetState = modalBottomSheetState
            ) {
                Column {
                    SettingsMenuButton(icon = {
                        Image(
                            painter = painterResource(R.drawable.turboself),
                            contentDescription = "Turboself",
                            modifier = Modifier.size(32.dp)
                        )
                    }, label = "Turboself", description = null) {
                        showServicesModalBottomSheet = false
                        navController.navigate(TurboselfLogin) { launchSingleTop = true }
                    }
                }
            }
    }
}