package com.example.vms.requestdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.vms.home.requests.RequestType
import com.example.vms.model.Guest
import com.example.vms.model.Visit
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import com.example.vms.user.User
import com.example.vms.visitdetails.VisitDetailsContent
import java.time.LocalDateTime

class RequestDetailsActivity : ComponentActivity() {
    private val viewModel: RequestDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisitorManagementSystemTheme {
                Scaffold(
                    topBar = {
                        TopBar(
                            requestName = stringResource(id = RequestType.HOST_CHANGE.description),
                            onBackPressed = { }
                        )
                    },
                    bottomBar = {
                        RequestDetailsButtons(
                            onAccept = { /*TODO*/ },
                            onDecline = {  }
                        )
                    }
                ) {
                    VisitDetailsContent(
                        modifier = Modifier.padding(it),
                        visit = testVisit
                    )
                }
            }
        }
    }

    companion object {
        private const val ARG_REQUEST_ID = "requestId"

        fun getLaunchIntent(context: Context, requestId: String): Intent {
            val intent = Intent(context, RequestDetailsActivity::class.java)
            intent.putExtra(ARG_REQUEST_ID, requestId)
            return intent
        }
    }
}

private val testGuests = listOf<Guest>(
    Guest(
        "michal@test.com",
        Guest.InvitationStatus.Accepted
    ),
    Guest(
        "bartek@test.com",
        Guest.InvitationStatus.Pending
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
        host = User(""),
        isCancelled = false
    )
