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
    button: BackButton,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (button) {
        BackButton.Gone -> Unit
        is BackButton.CountDown -> {
            Button(
                modifier = modifier,
                enabled = false,
                onClick = { }
            ) {
                Text("${button.secondsRemaining} s...")
            }
        }

        BackButton.Enabled -> {
            Button(
                modifier = modifier,
                onClick = onClick
            ) {
                Text(stringResource(R.string.back_to_home).uppercase())
            }
        }
    }
}