package com.example.vms.home

import androidx.annotation.StringRes
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.vms.R

@Composable
fun InfoDialog(
    @StringRes title: Int,
    @StringRes message: Int,
    onDismissDialog: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissDialog,
        title = { Text(stringResource(title)) },
        text = { Text(stringResource(message)) },
        confirmButton = { },
        dismissButton = {
            TextButton(onClick = onDismissDialog) {
                Text(stringResource(R.string.ok))
            }
        },
    )
}