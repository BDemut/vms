package com.example.vms.visitdetails

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vms.R
import com.example.vms.model.Visit

/**
 * Created by mśmiech on 24.08.2023.
 */

@Composable
fun LocationSection(
    room: Visit.Room,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(13.dp),
            imageVector = Icons.Default.LocationOn,
            contentDescription = stringResource(R.string.location_icon_content_description)
        )
        Text(text = room.name)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLocationSection() {
    LocationSection(room = Visit.Room("1", "Sala 101"))
}