package com.example.vms.home

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun LogoutDialog(
    onDismissDialog: () -> Unit,
    onLogoutClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismissDialog() },
        title = { Text("Logging out") },
        text = { Text("You will need to provide your credentials to access the app again") },
        confirmButton = {
            TextButton(onClick = { onLogoutClicked() }) {
                Text("LOGOUT")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissDialog() }) {
                Text("CANCEL")
            }
        },
    )
}