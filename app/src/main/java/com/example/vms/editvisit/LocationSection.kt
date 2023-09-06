package com.example.vms.editvisit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vms.R
import com.example.vms.editvisit.model.Visit.Room
/**
 * Created by mśmiech on 24.08.2023.
 */

@Composable
fun LocationSection(
    room: Room?,
    onClick: () -> Unit
) {
    val roomLabel = room?.name ?: stringResource(id = R.string.edit_visit_room_placeholder)
    Row {
        Icon(
            modifier = Modifier.padding(13.dp),
            imageVector = Icons.Default.LocationOn,
            contentDescription = stringResource(R.string.location_icon_content_description)
        )
        TextButton(onClick = { onClick() }) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(text = roomLabel)
            }
        }
    }
}

@Preview
@Composable
fun PreviewLocationSection() {
    LocationSection(room = null,
        onClick = {})
}