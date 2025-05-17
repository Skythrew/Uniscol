package io.github.skythrew.uniscol.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class Tab(
    val name: String,
    val icon: @Composable () -> ImageVector?,
    val iconSelected: @Composable () -> ImageVector?,
    val destination: Any
)
