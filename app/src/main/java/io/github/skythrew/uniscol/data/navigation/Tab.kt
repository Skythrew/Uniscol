package io.github.skythrew.uniscol.data.navigation

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabs")
data class Tab(
    @PrimaryKey
    val id: Int,
    val name: String,
    val icon: Int?,
    val iconSelected: Int?,
    val enabled: Boolean,
    val destination: String,
    val position: Int
)
