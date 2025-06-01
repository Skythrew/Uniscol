package io.github.skythrew.uniscol.presentation.mailbox

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MailboxState @Inject constructor() {
    val snackbarHostState = SnackbarHostState()

    fun showSnackbar(message: String, duration: SnackbarDuration, withDismiss: Boolean) {
        GlobalScope.launch {
            snackbarHostState.showSnackbar(message, duration = duration, withDismissAction = withDismiss)
        }
    }
}