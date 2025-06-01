package io.github.skythrew.uniscol.data.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.skythrew.uniscol.R

enum class TabIcon (val imageVector: @Composable () -> ImageVector) {
    HOME({Icons.Outlined.Home}),
    HOME_SELECTED({Icons.Default.Home}),

    SETTINGS({Icons.Outlined.Settings}),
    SETTINGS_SELECTED({Icons.Default.Settings}),

    RESTAURANT({ImageVector.vectorResource(R.drawable.outline_restaurant_24)}),
    RESTAURANT_SELECTED({ImageVector.vectorResource(R.drawable.baseline_restaurant_24)}),

    MAILBOX({Icons.Default.MailOutline}),
    MAILBOX_SELECTED({Icons.Default.Email})
}

@Entity(tableName = "tabs")
data class Tab(
    @PrimaryKey
    val id: Int,
    val name: String,
    val icon: TabIcon?,
    val iconSelected: TabIcon?,
    val enabled: Boolean,
    val destination: Route,
    val position: Int
)
