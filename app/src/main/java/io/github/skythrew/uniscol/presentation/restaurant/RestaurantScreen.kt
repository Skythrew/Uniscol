package io.github.skythrew.uniscol.presentation.restaurant

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lightspark.composeqr.DotShape
import com.lightspark.composeqr.QrCodeView
import io.github.skythrew.uniscol.R
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountFeature
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountInterface
import io.github.skythrew.uniscol.presentation.components.AccountSelector
import io.github.skythrew.uniscol.presentation.components.TopAppBarNavigation
import io.github.skythrew.uniscol.presentation.components.UniscolTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantScreen(navController: NavController, drawerState: DrawerState) {
    val viewModel: RestaurantViewModel = hiltViewModel()
    val accounts by viewModel.accounts.collectAsState(listOf())
    val account by viewModel.selectedAccount.collectAsState(null)

    val online by viewModel.networkRepository.online.collectAsState(false)

    val balance by viewModel.balance.collectAsState(null)

    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var showModalBottomSheet by remember { mutableStateOf(false) }

    val animatedBalance = balance?.let { animateIntAsState(balance!!) }

    /*val account by viewModel.selectedAccount.collectAsState(null)


    LaunchedEffect(account?.loggedIn) {
        Log.d("ACCOUNT", account?.loggedIn.toString())
    }

    LaunchedEffect(account) {
        if (account != null) {
            Log.d("COUCOU", "BEUH")
            viewModel.fetchBalance()
            Log.d("BALANCE", balance.toString())
            Log.d("ACCOUNT", account?.loggedIn.toString())
        }
    }*/

    Scaffold (
        topBar = {
            UniscolTopAppBar(
                title = "Cantine",
                navigation = TopAppBarNavigation.Sidebar,
                drawerState = drawerState,
                navController = navController,
                actions = {
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (account?.features?.contains(RestaurantAccountFeature.QRCode) == true)
                            IconButton(
                                onClick = {
                                    showModalBottomSheet = true
                                }
                            ) {
                                Icon(imageVector = ImageVector.vectorResource(R.drawable.baseline_qr_code_24), contentDescription = "QR Code")
                            }

                        account?.let {
                            AccountSelector(accounts ?: listOf(), account!!) {
                                viewModel.updateSelectedAccount(it as RestaurantAccountInterface)
                            }
                        }
                    }

                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (online)
                Text(animatedBalance?.value.toString())
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
            /*Column (
                modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Card {
                    Column (
                        modifier = Modifier.size(300.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column (
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Box {
                                QrCodeView(
                                    data = "1234",
                                    modifier = Modifier.size(220.dp),
                                    dotShape = DotShape.Circle
                                )
                            }

                            balance?.let {
                                Text("Solde: " + (balance!! / 100).toString() + "€", style = MaterialTheme.typography.headlineSmall)
                            }
                        }
                    }

                }
                Text(account?.label.toString())
                Text(balance.toString())
            }*/
        }


        if (account?.cardNumber != null && showModalBottomSheet)
            ModalBottomSheet(
                sheetState = modalBottomSheetState,
                onDismissRequest = {
                    showModalBottomSheet = false
                }
            ) {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    QrCodeView(
                        account!!.cardNumber!!,
                        modifier = Modifier.size(350.dp),
                        dotShape = DotShape.Circle
                    )
                }
            }
    }
}