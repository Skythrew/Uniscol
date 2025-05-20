package io.github.skythrew.uniscol.presentation.restaurant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import io.github.skythrew.uniscol.data.dates.UniscolLocalDateFormat
import io.github.skythrew.uniscol.presentation.components.AccountSelector
import io.github.skythrew.uniscol.presentation.components.TopAppBarNavigation
import io.github.skythrew.uniscol.presentation.components.UniscolTopAppBar
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantScreen(navController: NavController, drawerState: DrawerState) {
    val viewModel: RestaurantViewModel = hiltViewModel()

    val accounts by viewModel.accounts.collectAsState(listOf())
    val account by viewModel.selectedAccount.collectAsState(null)

    val online by viewModel.networkRepository.online.collectAsState(false)

    val bookings by viewModel.bookings.collectAsState(listOf())
    val bookingsDate by viewModel.bookingsDate.collectAsState()

    val balance by viewModel.balance.collectAsState(null)

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds()
    )

    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var showModalBottomSheet by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

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
                            AccountSelector(accounts, account!!) {
                                viewModel.updateSelectedAccount(it as RestaurantAccountInterface)
                            }
                        }
                    }

                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (online) {
                Column (
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                ) {
                    if (balance != null)
                        Box (
                            modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow).width(300.dp)
                        ) {
                            Column (
                                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row (
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) { Text("Solde:", style = MaterialTheme.typography.labelLarge)
                                    Text((balance!!.toDouble() / 100).toString() + "€") }
                            }
                        }

                    Box (
                        modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.surfaceContainerLow).width(300.dp)
                    ) {
                        Column (
                            modifier = Modifier.padding(8.dp).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row (
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FilledTonalIconButton(
                                    onClick = {
                                        viewModel.previousBookingsDate()
                                    }
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Date précédente")
                                }

                                FilledTonalButton(
                                    onClick = {
                                        showDatePicker = true
                                    }
                                ) {
                                    Text(LocalDate.parse(bookingsDate).format(UniscolLocalDateFormat))
                                }
                                FilledTonalIconButton(
                                    onClick = {
                                        viewModel.nextBookingsDate()
                                    }
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = "Date suivante"
                                    )
                                }
                            }

                            Column (
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (bookings.isEmpty())
                                    Text("Aucune réservation disponible")
                                bookings.forEach { booking ->
                                    booking.choices.forEachIndexed {index, choice ->
                                        Row (
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                choice.label ?: ("Choix " + (index + 1).toString()), style = MaterialTheme.typography.labelMedium)

                                            Switch(
                                                enabled = choice.enabled,
                                                checked = choice.booked,
                                                onCheckedChange = {
                                                    viewModel.toggleBookingChoice(choice.id, !choice.booked)
                                                }
                                            )
                                        }
                                    }
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

        if (showDatePicker)
            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDatePicker = false

                        if (datePickerState.selectedDateMillis != null)
                            viewModel.updateBookingsDate(Instant.fromEpochMilliseconds(datePickerState.selectedDateMillis!!))
                    }) {
                        Text("OK")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
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