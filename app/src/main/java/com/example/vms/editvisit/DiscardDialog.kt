package com.example.vms.editvisit

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.vms.R

/**
 * Created by mśmiech on 23.08.2023.
 */
@Composable
fun DiscardDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isNewVisit: Boolean
) {
    val descriptionResId = if (isNewVisit) {
        R.string.edit_visit_discard_dialog_description_new_visit
    } else {
        R.string.edit_visit_discard_dialog_description_edit_visit
    }
    AlertDialog(
        onDismissRequest = { onDismiss() },
        text = { Text(stringResource(descriptionResId)) },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(stringResource(R.string.edit_visit_discard_dialog_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.edit_visit_discard_dialog_cancel))
            }
        },
    )
}