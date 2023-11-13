package com.example.vms.guest.summary

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vms.R

@Composable
fun SummaryButton(
    secondsRemaining: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        enabled = true,
        onClick = onClick
    ) {
        Text("${stringResource(R.string.back_to_home).uppercase()} $secondsRemaining s.")
    }
}