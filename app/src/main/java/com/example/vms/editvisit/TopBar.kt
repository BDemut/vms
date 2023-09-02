package com.example.vms.editvisit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
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
    onSaveClick: () -> Unit
) {
    TopAppBar {
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
            TextButton(
                onClick = {
                    onSaveClick()
                }, modifier = Modifier.padding(16.dp, 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.edit_visit_save_button_label)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTopBar() {
    TopBar(onDiscardClick = {},
        onSaveClick = {}
    )
}