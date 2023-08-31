package com.example.vms.visitdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vms.R

/**
 * Created by mśmiech on 31.08.2023.
 */
@Preview(showBackground = true)
@Composable
fun CancelledSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.error)
    ) {
        Icon(
            modifier = Modifier.padding(13.dp),
            imageVector = Icons.Default.Cancel,
            tint = MaterialTheme.colors.onError,
            contentDescription = stringResource(R.string.location_icon_content_description)
        )
        Text(
            text = "Cancelled",
            color = MaterialTheme.colors.onError
        )
    }
}