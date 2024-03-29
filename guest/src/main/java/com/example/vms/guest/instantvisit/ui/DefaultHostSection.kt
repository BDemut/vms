package com.example.vms.guest.instantvisit.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.vms.R
import com.example.vms.guest.instantvisit.CONTENT_WIDTH
import dagger.Provides

@Composable
fun DefaultHostSection(
    isSelected: Boolean,
    onChecked: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .width(CONTENT_WIDTH),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(checked = isSelected, onCheckedChange = onChecked)
        Text(stringResource(R.string.default_host))
    }
}

@Composable
@Preview
fun SelectedDefaultHostPreview() {
    MaterialTheme {
        Surface(color = MaterialTheme.colors.background) {
            DefaultHostSection(isSelected = true, onChecked = {})
        }
    }
}

@Composable
@Preview
fun NotSelectedDefaultHostPreview() {
    MaterialTheme {
        Surface(color = MaterialTheme.colors.background) {
            DefaultHostSection(isSelected = false, onChecked = {})
        }
    }
}