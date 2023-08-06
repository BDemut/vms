package com.example.vms.home

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.vms.R

@Composable
fun LogoutDialog(
    onDismissDialog: () -> Unit,
    onLogoutClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissDialog() },
        title = { Text(stringResource(R.string.logout_dialog_title)) },
        text = { Text(stringResource(R.string.logout_dialog_description)) },
        confirmButton = {
            TextButton(onClick = { onLogoutClicked() }) {
                Text(stringResource(R.string.logout_dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissDialog() }) {
                Text(stringResource(R.string.logout_dialog_cancel))
            }
        },
    )
}