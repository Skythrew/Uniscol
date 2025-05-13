package io.github.skythrew.uniscol

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import io.github.skythrew.uniscol.data.UniscolDatabase
import io.github.skythrew.uniscol.data.accounts.Account
import io.github.skythrew.uniscol.data.accounts.AccountFeature
import io.github.skythrew.uniscol.data.accounts.AccountRepository
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountInterface
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountRepository
import io.github.skythrew.uniscol.data.services.ServiceType
import io.github.skythrew.uniscol.presentation.UniscolViewModel
import io.github.skythrew.uniscol.presentation.navigation.DrawerContent
import io.github.skythrew.uniscol.presentation.navigation.HomeTab
import io.github.skythrew.uniscol.presentation.navigation.SettingsTab
import io.github.skythrew.uniscol.presentation.navigation.Tab
import io.github.skythrew.uniscol.presentation.home.HomeScreen
import io.github.skythrew.uniscol.presentation.home.HomeViewModel
import io.github.skythrew.uniscol.presentation.navigation.AccountsSettings
import io.github.skythrew.uniscol.presentation.navigation.Canteen
import io.github.skythrew.uniscol.presentation.navigation.RestaurantSettingsTab
import io.github.skythrew.uniscol.presentation.navigation.Root
import io.github.skythrew.uniscol.presentation.navigation.TurboselfLogin
import io.github.skythrew.uniscol.presentation.restaurant.RestaurantScreen
import io.github.skythrew.uniscol.presentation.restaurant.RestaurantViewModel
import io.github.skythrew.uniscol.presentation.settings.SettingsScreen
import io.github.skythrew.uniscol.presentation.settings.accounts.AccountsSettingsScreen
import io.github.skythrew.uniscol.presentation.settings.accounts.AccountsSettingsViewModel
import io.github.skythrew.uniscol.presentation.settings.accounts.turboself.TurboselfLoginScreen
import io.github.skythrew.uniscol.presentation.settings.accounts.turboself.TurboselfLoginViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {

            val coroutineScope = rememberCoroutineScope()
            val navController = rememberNavController()

            val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = currentNavBackStackEntry?.destination

            val drawerState = rememberDrawerState(DrawerValue.Open)

            val viewModel: UniscolViewModel by viewModels()

            val accounts: List<Account> by viewModel.accounts.collectAsState(listOf())
            val restaurantAccounts: List<RestaurantAccountInterface> by viewModel.restaurantAccounts.collectAsState(listOf())

            var tabs: List<Tab> by remember { mutableStateOf(listOf()) }

            LaunchedEffect(restaurantAccounts.size) {
                val buildingTabs: MutableList<Tab> = mutableListOf()

                if (restaurantAccounts.isNotEmpty()) {
                    Log.d("ACCOUNT CARD", restaurantAccounts[0].cardNumber.toString())
                    buildingTabs.add(
                        Tab(
                            name = "Cantine",
                            icon = R.drawable.outline_restaurant_24,
                            iconSelected = R.drawable.baseline_restaurant_24,
                            destination = Canteen
                        )
                    )
                }

                tabs = buildingTabs
            }

            MaterialTheme {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet (
                            modifier = Modifier.navigationBarsPadding()
                        ) {
                            DrawerContent(
                                tabs,
                                { _: String?, destination: Any ->
                                    if (currentDestination?.hasRoute(destination::class) != true) {
                                        navController.navigate(destination) {
                                            popUpTo(navController.graph.id) {
                                                inclusive = true
                                            }
                                        }
                                    }

                                    coroutineScope.launch {
                                        drawerState.close()
                                    }
                                },
                                currentDestination
                            )
                        }

                    }
                ) {
                    NavHost(navController = navController, startDestination = HomeTab) {

                        composable<HomeTab> {
                            HomeScreen(navController, drawerState, HomeViewModel(viewModel, navController))
                        }

                        composable<SettingsTab> {
                            SettingsScreen(navController, drawerState)
                        }

                        composable<AccountsSettings> (
                            enterTransition = {
                                fadeIn(
                                    animationSpec = tween(
                                        200, easing = LinearEasing
                                    )
                                ) + slideIntoContainer(
                                    animationSpec = tween(200, easing = EaseIn),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                                )
                            }
                        ) {
                            AccountsSettingsScreen(navController, drawerState)
                        }

                        composable<TurboselfLogin> {
                            TurboselfLoginScreen(navController, drawerState)
                        }

                        composable<Canteen> {
                            RestaurantScreen(navController, drawerState)
                        }
                    }
                }
            }
        }
    }
}
