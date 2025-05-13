package io.github.skythrew.uniscol.presentation.navigation

data class Tab(
    val name: String,
    val icon: Int?,
    val iconSelected: Int?,
    val destination: Any
)
