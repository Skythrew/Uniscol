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
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute

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
        tabs.forEach { tab ->
            DrawerItem(
                label = tab.name,
                icon = tab.icon(),
                iconSelected = tab.iconSelected(),
                destination = tab.destination,
                currentDestination = currentDestination,
                navigate = navigateToTab
            )
        }
    }
}