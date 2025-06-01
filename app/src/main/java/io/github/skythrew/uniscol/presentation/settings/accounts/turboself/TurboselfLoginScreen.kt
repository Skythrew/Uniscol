package io.github.skythrew.uniscol.presentation.settings.accounts.turboself

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.github.skythrew.uniscol.R
import io.github.skythrew.uniscol.presentation.components.TopAppBarNavigation
import io.github.skythrew.uniscol.presentation.components.UniscolTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TurboselfLoginScreen(navController: NavController, drawerState: DrawerState) {
    val viewModel: TurboselfLoginViewModel = hiltViewModel()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginError by viewModel.loginError.collectAsState()
    val loginErrorMessage = "Identifiants incorrects"

    val isLogging by viewModel.isLogging.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Scaffold (
        topBar = {
            UniscolTopAppBar(
                title = "Connexion à Turboself",
                navigation = TopAppBarNavigation.Back,
                drawerState = drawerState,
                navController = navController
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxWidth().fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLogging)
                CircularProgressIndicator()
            else
                Column (
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painter = painterResource(R.drawable.turboself), contentDescription = "Logo Turboself", modifier = Modifier.size(92.dp))
                    Column (
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Nom d'utilisateur") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Nom d'utilisateur")
                            },
                            isError = loginError,
                            supportingText = {
                                if (loginError)
                                    Text(loginErrorMessage)
                            }
                        )

                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Mot de passe") },
                            visualTransformation = PasswordVisualTransformation(),
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = "Nom d'utilisateur")
                            },
                            isError = loginError,
                            supportingText = {
                                if (loginError)
                                    Text(loginErrorMessage)
                            }
                        )

                        Button(onClick = {
                                viewModel.login(username, password)
                        }) {
                            Text("Connexion")
                        }
                    }
                }
        }
    }
}