package io.github.skythrew.uniscol.presentation.mailbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import io.github.skythrew.uniscol.R
import io.github.skythrew.uniscol.data.accounts.conversation.ConversationAccountInterface
import io.github.skythrew.uniscol.data.accounts.conversation.Message
import io.github.skythrew.uniscol.data.navigation.Routes
import io.github.skythrew.uniscol.presentation.components.AccountSelector
import io.github.skythrew.uniscol.presentation.components.TopAppBarNavigation
import io.github.skythrew.uniscol.presentation.components.UniscolTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MailboxScreen(navController: NavController, drawerState: DrawerState) {
    val viewModel: MailboxViewModel = hiltViewModel()

    val accounts by viewModel.accounts.collectAsState(listOf())
    val account by viewModel.selectedAccount.collectAsState(null)

    val online by viewModel.networkRepository.online.collectAsState(false)

    val messages by viewModel.messages.collectAsState()

    Scaffold (
        snackbarHost = {
            SnackbarHost(viewModel.snackbarHostState)
        },
        topBar = {
            UniscolTopAppBar(
                title = "Messagerie",
                navigation = TopAppBarNavigation.Sidebar,
                drawerState = drawerState,
                navController = navController,
                actions = {
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        account?.let {
                            AccountSelector(accounts, account!!) {
                                viewModel.updateSelectedAccount(it as ConversationAccountInterface)
                            }
                        }
                    }

                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (online) {
                LazyColumn (
                    state = viewModel.lazyListState,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(messages, key = {message: Message -> message.id}) { message ->
                        Box (
                            modifier = Modifier.clickable {
                                navController.navigate(Routes.Message(id = message.id, title = message.subject)) {
                                    launchSingleTop = true
                                }
                            }
                        ) {
                            Row (
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                AsyncImage(
                                    modifier = Modifier.clip(CircleShape).size(32.dp),
                                    model = message.authorPictureUrl,
                                    contentDescription = ""
                                )
                                Column (
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(message.author, fontWeight = if (message.unread) FontWeight.Bold else FontWeight.Normal)
                                    Text(message.subject, fontWeight = if (message.unread) FontWeight.Bold else FontWeight.Normal)
                                }
                            }
                        }

                    }
                }
            }
            else
                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                ) {
                    Column (
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.baseline_cloud_off_24),
                            contentDescription = "offline",
                            modifier = Modifier.size(92.dp)
                        )

                        Text("Vous êtes hors-ligne.\nConnectez-vous à Internet pour voir plus d'informations.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center)
                    }
                }
        }
    }
}