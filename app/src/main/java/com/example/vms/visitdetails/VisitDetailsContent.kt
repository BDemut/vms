package com.example.vms.visitdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vms.model.Guest
import com.example.vms.model.Visit
import com.example.vms.user.User
import java.time.LocalDateTime

@Composable
fun VisitDetailsContent(
    visit: Visit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(50.dp))
            Column {
                Title(text = visit.title)
                VisitDateTime(start = visit.start, end = visit.end)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
        if (visit.isCancelled) {
            CancelledSection()
        }
        if (visit.room != null) {
            LocationSection(room = visit.room)
        }
        HostSection(user = visit.host)
        GuestsSection(guests = visit.guests)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVisitDetailsContent() {
    VisitDetailsContent(testVisit)
}

private val testGuests = listOf<Guest>(
    Guest(
        "michal@test.com",
        Guest.InvitationStatus.Accepted,
        null
    ),
    Guest(
        "bartek@test.com",
        Guest.InvitationStatus.Pending,
        "Bartek"
    ),
)

private val testVisit =
    Visit(
        id = "",
        title = "Title",
        start = LocalDateTime.now(),
        end = LocalDateTime.now().plusHours(1),
        room = Visit.Room("1", "Sala 101"),
        guests = testGuests,
        host = User("michal@test.com"),
        isCancelled = false
    )
