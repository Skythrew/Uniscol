package io.github.skythrew.uniscol.presentation.mailbox

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class MessageAttachmentContract : ActivityResultContract<String, Uri?>() {
    override fun createIntent(
        context: Context,
        input: String
    ): Intent {
        return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_TITLE, input)
        }
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ): Uri? {
        return intent.takeIf {
            resultCode == Activity.RESULT_OK
        }?.data
    }
}