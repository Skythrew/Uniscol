package io.github.skythrew.uniscol.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerItem(
    label: String,
    icon: ImageVector? = null,
    iconSelected: ImageVector? = null,
    destination: Any,
    currentDestination: NavDestination?,
    navigate: (destinationName: String?, destination: Any) -> Unit
) {
    val selected = currentDestination?.hasRoute(destination::class) ?: false
    NavigationDrawerItem(
        label = { Text(label) },
        icon = {
            when(selected) {
                true -> iconSelected?.let { Icon(imageVector = iconSelected, contentDescription = label) }
                false -> icon?.let { Icon(imageVector = icon, contentDescription = label) }
            }
        },
        selected = selected,
        onClick = { navigate(label, destination) },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable
fun DrawerContent(
    tabs: List<Tab>,
    navigateToTab: (destinationName: String?, destination: Any) -> Unit,
    currentDestination: NavDestination?
) {
    Column (
        modifier = Modifier.padding(vertical = 5.dp),
    ) {
        /* Hardcoded tabs: Home and Settings */
        DrawerItem(
            label = "Accueil",
            icon = Icons.Outlined.Home,
            iconSelected = Icons.Filled.Home,
            destination = HomeTab,
            currentDestination = currentDestination,
            navigate = navigateToTab
        )

        DrawerItem(
            label = "Paramètres",
            icon = Icons.Outlined.Settings,
            iconSelected = Icons.Filled.Settings,
            destination = SettingsTab,
            currentDestination = currentDestination,
            navigate = navigateToTab
        )

        if (tabs.isNotEmpty())
            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp, horizontal = 28.dp))

        tabs.forEach { tab ->
            DrawerItem(
                label = tab.name,
                icon = tab.icon?.let { ImageVector.vectorResource(tab.icon) },
                iconSelected = tab.iconSelected?.let { ImageVector.vectorResource(tab.iconSelected) },
                destination = tab.destination,
                currentDestination = currentDestination,
                navigate = navigateToTab
            )
        }
    }
}