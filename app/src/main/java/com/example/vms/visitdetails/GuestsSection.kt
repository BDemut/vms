package com.example.vms.visitdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vms.R
import com.example.vms.model.Guest

/**
 * Created by mśmiech on 24.08.2023.
 */

@Composable
fun GuestsSection(
    guests: List<Guest>
) {
    val focusRequester = remember { FocusRequester() }
    Row {
        Icon(
            modifier = Modifier.padding(13.dp),
            imageVector = Icons.Default.People,
            contentDescription = stringResource(R.string.guests_icon_content_description)
        )
        Column {
            Text(
                text = pluralStringResource(
                    id = R.plurals.visit_details_guests_count,
                    count = guests.size,
                    guests.size
                ),
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            guests.forEach {
                Guest(guest = it)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun PreviewGuestsSection() {
    GuestsSection(
        guests = testGuests
    )
}

@Composable
private fun Guest(guest: Guest) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(end = 8.dp, top = 13.dp, bottom = 13.dp),
            imageVector = getInvitationStatusIcon(guest.invitationStatus),
            contentDescription = stringResource(R.string.invitation_status_icon_content_description),
            tint = getInvitationStatusIconColor(guest.invitationStatus)
                ?: LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
        )
        if (guest.name == null) {
            Text(
                text = guest.email
            )
        } else {
            Column {
                Text(
                    text = guest.name,
                )
                Text(
                    text = guest.email,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    fontSize = 13.sp
                )
            }
        }
    }
}

private fun getInvitationStatusIcon(invitationStatus: Guest.InvitationStatus): ImageVector {
    return when (invitationStatus) {
        Guest.InvitationStatus.Accepted -> Icons.Default.CheckCircle
        Guest.InvitationStatus.Declined -> Icons.Default.RemoveCircle
        Guest.InvitationStatus.Pending -> Icons.Default.Pending
    }
}

private fun getInvitationStatusIconColor(invitationStatus: Guest.InvitationStatus): Color? {
    return when (invitationStatus) {
        Guest.InvitationStatus.Accepted -> Color(0xFF00C853)
        Guest.InvitationStatus.Declined -> Color(0xFFD50000)
        Guest.InvitationStatus.Pending -> null
    }
}

private val testGuests = listOf(
    Guest("michal@test.com", Guest.InvitationStatus.Accepted, null),
    Guest("bartek@test.com", Guest.InvitationStatus.Pending, "Michał"),
    Guest("bartek@test.com", Guest.InvitationStatus.Declined, "Bartek"),
)