package com.example.vms.auditlog

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

/**
 * Created by mśmiech on 12.09.2023.
 */
@Composable
fun TopBar(
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
            text = stringResource(R.string.audit_log_title),
            fontWeight = FontWeight.Bold
        )
    }
}