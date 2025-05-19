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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import io.github.skythrew.uniscol.data.navigation.Tab

@Composable
fun DrawerItem(
    label: String,
    icon: ImageVector? = null,
    iconSelected: ImageVector? = null,
    destination: String,
    currentDestination: NavDestination?,
    navigate: (destination: String) -> Unit
) {
    val selected = currentDestination?.route == destination
    NavigationDrawerItem(
        label = { Text(label) },
        icon = {
            when(selected) {
                true -> iconSelected?.let { Icon(imageVector = iconSelected, contentDescription = label) }
                false -> icon?.let { Icon(imageVector = icon, contentDescription = label) }
            }
        },
        selected = selected,
        onClick = { navigate(destination) },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable
fun DrawerContent(
    tabs: List<Tab>,
    navigateToTab: (destination: String) -> Unit,
    currentDestination: NavDestination?
) {
    Column (
        modifier = Modifier.padding(vertical = 5.dp),
    ) {
        tabs.forEach { tab ->
            DrawerItem(
                label = tab.name,
                icon = tab.icon?.let { ImageVector.vectorResource(it) },
                iconSelected = tab.iconSelected?.let { ImageVector.vectorResource(it) },
                destination = tab.destination,
                currentDestination = currentDestination,
                navigate = navigateToTab
            )
        }
    }
}