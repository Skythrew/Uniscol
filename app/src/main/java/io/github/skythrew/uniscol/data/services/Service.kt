package io.github.skythrew.uniscol.data.services

import androidx.compose.ui.graphics.vector.ImageVector

enum class ServiceType {
    CANTEEN
}

enum class Services {
    Turboself
}

data class Service (
    val id: String,
    val name: String,
    val icon: ImageVector?,
    val description: String?,
    val tabs: List<Any>,
    val type: ServiceType
)
