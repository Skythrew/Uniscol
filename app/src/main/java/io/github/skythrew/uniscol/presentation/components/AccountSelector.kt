package io.github.skythrew.uniscol.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.skythrew.uniscol.data.accounts.AccountInterface
import io.github.skythrew.uniscol.data.services.ServiceRepository

@Composable
fun AccountSelector(
    accounts: List<AccountInterface>,
    selectedAccount: AccountInterface,
    onAccountSelected: (account: Any) -> Unit
) {
    val serviceRepository = ServiceRepository()

    var expanded by remember { mutableStateOf(false) }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button (
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp)),
            colors = ButtonDefaults.filledTonalButtonColors(),
            onClick = {
                expanded = !expanded
            },
            contentPadding = ButtonDefaults.TextButtonWithIconContentPadding
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(5.dp)
            ) {
                Image(
                    painter = painterResource(serviceRepository.getIconForService(selectedAccount.service)),
                    contentDescription = "Icône service",
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    selectedAccount.label ?: selectedAccount.username ?: selectedAccount.service.name,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        DropdownMenu (
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            accounts.forEach { account ->
                DropdownMenuItem(
                    text = {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Image(
                                painter = painterResource(serviceRepository.getIconForService(account.service)),
                                contentDescription = "Icône service",
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                account.label ?: account.username ?: account.service.name,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    },
                    onClick = {
                        onAccountSelected(account)
                        expanded = !expanded
                    }
                )
            }
        }
    }

}