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
    isMoreOptionsShowing: Boolean,
    isEditButtonShowing: Boolean,
) {
    var showMoreOptionsDropdownMenu by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DiscardButton(onClick = onDiscardClick)
        Row {
            if (isEditButtonShowing) {
                EditButton(onClick = onEditClick)
            }
            if (isMoreOptionsShowing) {
                MoreOptionsButton(
                    onClick = {
                        showMoreOptionsDropdownMenu = !showMoreOptionsDropdownMenu
                    }
                )
                MoreOptionsDropdownMenu(
                    expanded = showMoreOptionsDropdownMenu,
                    onDismissRequest = { showMoreOptionsDropdownMenu = false },
                    onChangeHostClick = {
                        showMoreOptionsDropdownMenu = false
                        onChangeHostClick()
                    },
                    onCancelVisitClick = {
                        showMoreOptionsDropdownMenu = false
                        onCancelVisitClick()
                    },
                )
            }
        }
    }
}

@Composable
fun DiscardButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(8.dp, 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(R.string.discard_icon_content_description)
        )
    }
}

@Composable
fun EditButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(8.dp, 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = stringResource(R.string.discard_icon_content_description)
        )
    }
}

@Composable
fun MoreOptionsButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(8.dp, 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.discard_icon_content_description)
        )
    }
}

@Composable
fun MoreOptionsDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onChangeHostClick: () -> Unit,
    onCancelVisitClick: () -> Unit,
    ) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest) {
        DropdownMenuItem(onClick = onChangeHostClick) {
            Text(text = stringResource(id = R.string.visit_details_change_host))
        }
        DropdownMenuItem(onClick = onCancelVisitClick) {
            Text(text = stringResource(id = R.string.visit_details_cancel_visit))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTopBar() {
    TopBar(
        onDiscardClick = {},
        onEditClick = {},
        onChangeHostClick = {},
        onCancelVisitClick = {},
        isMoreOptionsShowing = true,
        isEditButtonShowing = true,
    )
}