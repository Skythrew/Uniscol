package io.github.skythrew.uniscol.presentation.settings.accounts.edifice

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.github.skythrew.uniscol.presentation.components.TopAppBarNavigation
import io.github.skythrew.uniscol.presentation.components.UniscolTopAppBar


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EdificeLoginScreen(navController: NavController, drawerState: DrawerState) {
    val viewModel: EdificeLoginViewModel = hiltViewModel()

    val selectedInstance by viewModel.selectedInstance.collectAsState()
    val showWebview by viewModel.showWebview.collectAsState()

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            UniscolTopAppBar(
                title = "Connexion à " + (selectedInstance?.displayName ?: "NEO/ONE"),
                navigation = TopAppBarNavigation.Back,
                drawerState = drawerState,
                navController = navController
            )
        }
    ) { padding ->
        if (showWebview && selectedInstance !== null)
            AndroidView(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                factory = { context ->
                    viewModel.webView(context)
                },
                update = { webView ->
                    if (selectedInstance != null)
                        webView.loadUrl(selectedInstance!!.url)
                }
            )
        else
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FlowRow(
                    modifier = Modifier.verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    viewModel.instances.forEach { instance ->
                        ElevatedCard(
                            onClick = {
                                viewModel.selectInstance(instance.key)
                            },
                            modifier = Modifier.size(150.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painterResource(instance.value.logo),
                                    contentDescription = instance.key
                                )
                            }

                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(instance.key, modifier = Modifier.padding(top = 8.dp))
                            }
                        }
                    }
                }
            }
    }
}