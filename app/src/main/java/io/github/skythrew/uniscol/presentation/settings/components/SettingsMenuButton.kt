package io.github.skythrew.uniscol.presentation.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun SettingsMenuButton(
    icon: @Composable () -> Unit = {},
    label: String,
    labelStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    description: String?,
    onClick: () -> Unit
) {
    Row (
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row (
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon()

                Column {
                    Text(label, style = labelStyle)

                    description?.let {
                        Text(description, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "")
        }

    }
}