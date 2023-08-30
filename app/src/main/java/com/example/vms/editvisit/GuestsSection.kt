package com.example.vms.editvisit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vms.R
import com.example.vms.editvisit.model.Guest

/**
 * Created by mśmiech on 24.08.2023.
 */

@Composable
fun GuestsSection(
    guests: List<Guest>,
    onAddGuestButtonClicked: (String) -> Unit,
    onRemoveGuestButtonClicked: (Guest) -> Unit,
    newGuestEmail: String,
    onNewGuestEmailChange: (String) -> Unit,
    isNewGuestEmailError: Boolean,
    showNewGuestEmailClearInputButton: Boolean
) {
    Row {
        Icon(
            modifier = Modifier.padding(13.dp),
            imageVector = Icons.Default.People,
            contentDescription = stringResource(R.string.guests_icon_content_description)
        )
        Column {
            NewGuestEmailTextInputLayout(
                onAddGuestButtonClicked = onAddGuestButtonClicked,
                newGuestEmail = newGuestEmail,
                onNewGuestEmailChange = onNewGuestEmailChange,
                isNewGuestEmailError = isNewGuestEmailError,
                showNewGuestEmailClearInputButton = showNewGuestEmailClearInputButton
            )
            GuestList(
                guests = guests,
                onRemoveGuestButtonClicked = onRemoveGuestButtonClicked
            )
        }
    }
}

@Composable
fun NewGuestEmailTextInputLayout(
    onAddGuestButtonClicked: (String) -> Unit,
    newGuestEmail: String,
    onNewGuestEmailChange: (String) -> Unit,
    isNewGuestEmailError: Boolean,
    showNewGuestEmailClearInputButton: Boolean
) {
    val focusRequester = remember { FocusRequester() }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .padding(start = 10.dp)
                .weight(1.0f)
        ) {
            NoShapeTextField(
                value = newGuestEmail,
                onValueChange = { onNewGuestEmailChange(it) },
                placeholder = {
                    Text(
                        text = stringResource(R.string.edit_visit_add_guest_placeholder),
                        color = MaterialTheme.colors.primary,
                    )
                },
                modifier = Modifier
                    .focusRequester(focusRequester),
                isError = isNewGuestEmailError
            )
            if (isNewGuestEmailError) {
                Text(
                    text = stringResource(R.string.new_guest_email_error),
                    modifier = Modifier
                        .width(200.dp)
                        .align(Alignment.BottomStart),
                    color = MaterialTheme.colors.error
                )
            }
        }
        Row {
            if (showNewGuestEmailClearInputButton) {
                ClearInputButton(onClick = {
                    onNewGuestEmailChange("")
                })
            }
            AddGuestButton(onClick = {
                focusRequester.requestFocus()
                onAddGuestButtonClicked(newGuestEmail)
            })
        }
    }
}

@Composable
fun ClearInputButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = stringResource(R.string.clear_content_description),
        )
    }
}

@Composable
fun AddGuestButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_guest_content_description),
            tint = MaterialTheme.colors.primary,
        )
    }
}

@Composable
private fun GuestList(guests: List<Guest>, onRemoveGuestButtonClicked: (Guest) -> Unit) {
    guests.forEach {
        Guest(guest = it, onRemoveGuestButtonClicked)
    }
}

@Composable
private fun Guest(guest: Guest, onRemoveGuestButtonClicked: (Guest) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = guest.email, modifier = Modifier.padding(8.dp, 8.dp)
        )
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = stringResource(R.string.remove_guest_content_description),
            modifier = Modifier
                .padding(13.dp)
                .clickable {
                    onRemoveGuestButtonClicked(guest)
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGuestsSection() {
    GuestsSection(
        guests = testGuests,
        onAddGuestButtonClicked = {},
        onRemoveGuestButtonClicked = {},
        newGuestEmail = "abc",
        onNewGuestEmailChange = {},
        isNewGuestEmailError = false,
        showNewGuestEmailClearInputButton = false
    )
}

private val testGuests = listOf(
    Guest("michal@test.com"),
    Guest("bartek@test.com"),
)