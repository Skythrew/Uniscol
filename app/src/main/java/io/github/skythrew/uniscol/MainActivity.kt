package io.github.skythrew.uniscol

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import io.github.skythrew.uniscol.data.accounts.conversation.ConversationAccountInterface
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountInterface
import io.github.skythrew.uniscol.data.navigation.MailboxRoute
import io.github.skythrew.uniscol.data.navigation.RestaurantRoute
import io.github.skythrew.uniscol.data.navigation.Routes
import io.github.skythrew.uniscol.presentation.UniscolTheme
import io.github.skythrew.uniscol.presentation.UniscolViewModel
import io.github.skythrew.uniscol.presentation.home.HomeScreen
import io.github.skythrew.uniscol.presentation.mailbox.MailboxScreen
import io.github.skythrew.uniscol.presentation.mailbox.MessageScreen
import io.github.skythrew.uniscol.presentation.navigation.DrawerContent
import io.github.skythrew.uniscol.presentation.restaurant.RestaurantScreen
import io.github.skythrew.uniscol.presentation.settings.SettingsScreen
import io.github.skythrew.uniscol.presentation.settings.accounts.AccountsSettingsScreen
import io.github.skythrew.uniscol.presentation.settings.accounts.edifice.EdificeLoginScreen
import io.github.skythrew.uniscol.presentation.settings.accounts.turboself.TurboselfLoginScreen
import io.github.skythrew.uniscol.presentation.settings.ui.InterfaceSettingsScreen
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerForActivityResult(CreateDocument("*")) { uri: Uri? ->
            println(uri.toString())
        }

        enableEdgeToEdge()
        setContent {

            val coroutineScope = rememberCoroutineScope()
            val navController = rememberNavController()

            val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = currentNavBackStackEntry?.destination

            val drawerState = rememberDrawerState(DrawerValue.Closed)

            val viewModel: UniscolViewModel by viewModels()

            val conversationAccounts: List<ConversationAccountInterface> by viewModel.conversationAccounts.collectAsState(
                listOf()
            )
            val restaurantAccounts: List<RestaurantAccountInterface> by viewModel.restaurantAccounts.collectAsState(
                listOf()
            )
            val tabs by viewModel.tabs.collectAsState(listOf())

            LaunchedEffect(restaurantAccounts.isEmpty()) {
                if (restaurantAccounts.isEmpty()) {
                    viewModel.disableTab(RestaurantRoute().name)
                } else {
                    viewModel.enableTab(RestaurantRoute().name)
                }
            }

            LaunchedEffect(conversationAccounts.isEmpty()) {
                if (conversationAccounts.isEmpty()) {
                    viewModel.disableTab(MailboxRoute().name)
                } else {
                    viewModel.enableTab(MailboxRoute().name)
                }
            }

            UniscolTheme {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet(
                            modifier = Modifier.navigationBarsPadding()
                        ) {
                            DrawerContent(
                                navController,
                                tabs
                            ) { destination: Any ->
                                if (currentDestination?.hasRoute(destination::class) == false) {
                                    navController.navigate(destination) {
                                        popUpTo(navController.graph.id) {
                                            inclusive = true
                                        }
                                    }
                                }

                                coroutineScope.launch {
                                    drawerState.close()
                                }
                            }
                        }

                    }
                ) {
                    NavHost(navController = navController, startDestination = Routes.Home) {
                        composable<Routes.Home> {
                            HomeScreen(navController, drawerState) {
                                navController.navigate(Routes.Settings)
                            }
                        }

                        composable<Routes.Settings> {
                            SettingsScreen(navController, drawerState)
                        }

                        composable<Routes.AccountSettings>(enterTransition = {
                            fadeIn(
                                animationSpec = tween(
                                    200, easing = LinearEasing
                                )
                            ) + slideIntoContainer(
                                animationSpec = tween(200, easing = EaseIn),
                                towards = AnimatedContentTransitionScope.SlideDirection.Start
                            )
                        }) {
                            AccountsSettingsScreen(navController, drawerState)
                        }

                        composable<Routes.TurboselfLogin> {
                            TurboselfLoginScreen(navController, drawerState)
                        }

                        composable<Routes.EdificeLogin> {
                            EdificeLoginScreen(navController, drawerState)
                        }

                        composable<Routes.Restaurant> {
                            RestaurantScreen(navController, drawerState)
                        }

                        composable<Routes.Mailbox> {
                            MailboxScreen(navController, drawerState)
                        }

                        composable<Routes.InterfaceSettings> {
                            InterfaceSettingsScreen(navController, drawerState)
                        }

                        composable<Routes.Message> {
                            val message: Routes.Message = it.toRoute()
                            MessageScreen(navController, drawerState, message.id)
                        }
                    }
                }
            }
        }
    }
}
