package com.example.vms.requestdetails

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.vms.R

@Composable
fun TopBar(
    requestName: String,
    onBackPressed: () -> Unit
) {
    TopAppBar {
        IconButton(onClick = onBackPressed) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back_icon_content_description)
            )
        }
        Text(
            text = requestName,
            fontWeight = FontWeight.Bold
        )
    }
}