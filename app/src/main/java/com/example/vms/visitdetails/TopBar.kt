package com.example.vms.visitdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vms.R

/**
 * Created by mśmiech on 24.08.2023.
 */
@Composable
fun TopBar(
    onDiscardClick: () -> Unit,
    onEditClick: () -> Unit,
    onChangeHostClick: () -> Unit,
    onCancelVisitClick: () -> Unit,
    showMoreOptions: Boolean
) {
    var showMenu by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {
                onDiscardClick()
            }, modifier = Modifier.padding(8.dp, 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.discard_icon_content_description)
            )
        }

        Row {
            IconButton(
                onClick = {
                    onEditClick()
                }, modifier = Modifier.padding(8.dp, 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.discard_icon_content_description)
                )
            }
            if (showMoreOptions) {
                IconButton(
                    onClick = {
                        showMenu = !showMenu
                    }, modifier = Modifier.padding(8.dp, 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.discard_icon_content_description)
                    )
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(onClick = {
                        showMenu = false
                        onChangeHostClick()
                    }) {
                        Text(text = "Change the host")
                    }
                    DropdownMenuItem(onClick = {
                        showMenu = false
                        onCancelVisitClick()
                    }) {
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTopBar() {
    TopBar(
        onDiscardClick = {},
        onEditClick = {},
        {},
        {},
        true
    )
}