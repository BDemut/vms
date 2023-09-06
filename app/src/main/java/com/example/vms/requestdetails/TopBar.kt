package com.example.vms.requestdetails

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TopBar(
    requestName: String,
    onBackPressed: () -> Unit
) {
    TopAppBar {
        IconButton(onClick = onBackPressed) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back arrow"
            )
        }
        Text(
            text = requestName,
            fontWeight = FontWeight.Bold
        )
    }
}