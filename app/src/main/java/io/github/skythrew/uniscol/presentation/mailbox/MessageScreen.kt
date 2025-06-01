package io.github.skythrew.uniscol.presentation.mailbox

import android.text.format.Formatter
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.github.skythrew.uniscol.R
import io.github.skythrew.uniscol.data.accounts.conversation.Attachment
import io.github.skythrew.uniscol.presentation.components.TopAppBarNavigation
import io.github.skythrew.uniscol.presentation.components.UniscolTopAppBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(navController: NavController, drawerState: DrawerState, messageId: String) {
    val viewModel: MessageViewModel = hiltViewModel()

    val account = viewModel.account

    val message by viewModel.message.collectAsState()
    val attachments by viewModel.attachments.collectAsState()
    val downloadingAttachments by viewModel.downloadingAttachments.collectAsState()

    val showDeleteDialog = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val filePicker = rememberLauncherForActivityResult(
        contract = MessageAttachmentContract(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.writeAttachment(uri)
            }
        }
    )

    LaunchedEffect(true) {
        viewModel.fetchMessage(messageId)
    }

    Scaffold (
        topBar = {
            UniscolTopAppBar(
                title = "",
                navigation = TopAppBarNavigation.Back,
                drawerState = drawerState,
                navController = navController,
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    navigationIconContentColor = Color.Black,
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
                actions = {
                    message?.let {
                        if (account!!.supportConversationToggleUnread)
                            IconButton(
                                onClick = {
                                    viewModel.toggleUnread()
                                }
                            ) {
                                Icon(painterResource(if (message!!.unread) R.drawable.outline_visibility_off_24 else R.drawable.outline_visibility_24), contentDescription = "Supprimer")
                            }

                        IconButton(
                            onClick = {
                                showDeleteDialog.value = true
                            }
                        ) {
                            Icon(Icons.Outlined.Delete, contentDescription = "Supprimer")
                        }

                        IconButton(
                            onClick = {}
                        ) {
                            Icon(Icons.Outlined.MoreVert, contentDescription = "Plus")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column (
            modifier = Modifier.padding(padding).verticalScroll(rememberScrollState())
        ) {
            Row (
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondaryContainer).padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(message?.subject ?: "", style = MaterialTheme.typography.headlineSmall)
            }

            if (message == null)
                LinearProgressIndicator()

            Text(AnnotatedString.fromHtml(message?.body.toString()), modifier = Modifier.padding(8.dp))

            Column (
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                attachments.forEach { attachment ->
                    OutlinedCard(
                        onClick = {
                            viewModel.selectAttachment(attachment)
                            filePicker.launch(attachment.name)
                        }
                    ) {
                        Box {
                            Column (
                                modifier = Modifier.padding(16.dp).fillMaxHeight().align(Alignment.CenterStart),
                                verticalArrangement = Arrangement.Center
                            ) {
                                if (downloadingAttachments.contains(attachment.id))
                                    CircularProgressIndicator()
                                else
                                    Icon(painterResource(R.drawable.outline_attachment_24), contentDescription = "Pièce jointe")
                            }

                            Row (
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Column (
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(attachment.name, style = MaterialTheme.typography.labelLarge)

                                    Text(Formatter.formatFileSize(LocalContext.current, attachment.size), style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    if (showDeleteDialog.value)
        BasicAlertDialog(
            onDismissRequest = {
                showDeleteDialog.value = false
            }
        ) {
            Surface (
                shape = MaterialTheme.shapes.large
            ) {
                Column (
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column (
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Suppression du message", style = MaterialTheme.typography.headlineSmall)

                        Text("Êtes-vous sûr de vouloir supprimer ce message ?")
                    }

                    Row (
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(
                            onClick = {
                                showDeleteDialog.value = false
                            }
                        ) {
                            Text("Annuler")
                        }

                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.deleteMessage()

                                    showDeleteDialog.value = false

                                    navController.popBackStack()

                                    viewModel.mailboxState.showSnackbar(
                                        "Message supprimé",
                                        withDismiss = true,
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                        ) {
                            Text("Supprimer")
                        }
                    }
                }
            }
        }
}