package io.github.skythrew.uniscol.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import io.github.skythrew.uniscol.data.navigation.Tab

@Composable
fun DrawerItem(
    label: String,
    icon: ImageVector? = null,
    iconSelected: ImageVector? = null,
    selected: Boolean,
    navigate: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(label) },
        icon = {
            when(selected) {
                true -> iconSelected?.let { Icon(iconSelected, contentDescription = label) }
                false -> icon?.let { Icon(icon, contentDescription = label) }
            }
        },
        selected = selected,
        onClick = navigate,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable
fun DrawerContent(
    navController: NavController,
    tabs: List<Tab>,
    navigateToTab: (destination: Any) -> Unit
) {
    Column (
        modifier = Modifier.padding(vertical = 5.dp),
    ) {
        tabs.forEachIndexed { index, tab ->
            DrawerItem(
                label = tab.name,
                icon = tab.icon?.imageVector?.let { it() },
                iconSelected = tab.iconSelected?.imageVector?.let { it() },
                selected = navController.currentDestination?.hasRoute(tab.destination.route::class) == true,
                navigate = {
                    navigateToTab(tab.destination.route)
                }
            )
        }
    }
}