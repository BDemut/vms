package com.example.vms.auditlog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vms.R

/**
 * Created by mśmiech on 12.09.2023.
 */
@Composable
fun BottomBar(
    onGenerateClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = onGenerateClick
    ) {
        Text(text = stringResource(R.string.generate_button))
    }
}