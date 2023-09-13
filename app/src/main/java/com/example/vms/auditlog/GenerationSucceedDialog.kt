package com.example.vms.auditlog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.vms.R

/**
 * Created by mśmiech on 12.09.2023.
 */
@Composable
fun GenerationSucceedDialog(
    onDismissDialog: () -> Unit,
    signInUserEmail: String
) {
    AlertDialog(
        onDismissRequest = { onDismissDialog() },
        title = { Text(stringResource(R.string.generation_succeed_dialog_title)) },
        text = { Text(stringResource(R.string.generation_succeed_dialog_text, signInUserEmail)) },
        confirmButton = {
            TextButton(onClick = { onDismissDialog() }) {
                Text(stringResource(android.R.string.ok))
            }
        },
    )
}