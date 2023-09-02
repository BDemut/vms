package com.example.vms.guest.instantvisit.ui

import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import com.example.vms.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HostChangeDropdown(
    currentHostName: String,
    availableHosts: List<String>,
    onHostSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isHostPickerExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isHostPickerExpanded,
        onExpandedChange = { isHostPickerExpanded = it }
    ) {
        CompositionLocalProvider(
            LocalTextInputService provides null
        ) {
            InstantVisitTextField(
                value = currentHostName,
                onValueChange = {},
                placeholder = stringResource(R.string.host_name_placeholder),
                readOnly = true
            )
        }
        ExposedDropdownMenu(
            expanded = isHostPickerExpanded,
            onDismissRequest = { isHostPickerExpanded = false }
        ) {
            availableHosts.forEach { hostName ->
                DropdownMenuItem(
                    onClick = {
                        onHostSelected(hostName)
                        isHostPickerExpanded = false
                    }
                ) {
                    Text(hostName)
                }
            }
        }
    }
}
