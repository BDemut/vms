package com.example.vms.visitdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vms.R
import com.example.vms.user.User

/**
 * Created by mśmiech on 06.09.2023.
 */

@Composable
fun HostSection(
    user: User,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(13.dp),
            imageVector = Icons.Default.Stars,
            contentDescription = stringResource(R.string.host_icon_content_description)
        )
        if (user.name == null) {
            Text(text = user.email)
        } else {
            Column {
                Text(
                    text = user.name,
                )
                Text(
                    text = user.email,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHostSection() {
    HostSection(User("michal@test.com", "Michał"))
}