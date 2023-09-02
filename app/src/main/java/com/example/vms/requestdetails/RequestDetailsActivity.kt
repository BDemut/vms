package com.example.vms.requestdetails

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vms.R
import com.example.vms.home.requests.RequestType
import com.example.vms.model.Guest
import com.example.vms.model.Room
import com.example.vms.model.Visit
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import com.example.vms.user.User
import com.example.vms.visitdetails.VisitDetailsContent
import java.time.LocalDateTime

class RequestDetailsActivity : ComponentActivity() {
    //private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisitorManagementSystemTheme {
                Scaffold(
                    topBar = {
                        RequestDetailsTopBar(
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
        room = Room("1", "Sala 101"),
        guests = testGuests,
        host = User(""),
        isCancelled = false
    )

@Composable
private fun RequestDetailsTopBar(
    requestName: String,
    onBackPressed: () -> Unit
) {
    TopAppBar {
        IconButton(onClick = onBackPressed) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back arrow"
            )
        }
        Text(
            text = requestName,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RequestDetailsButtons(
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = onAccept) {
            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_yes),
                contentDescription = stringResource(R.string.accept_request_button_content_description),
                tint = Color.Green
            )
        }
        IconButton(onClick = onDecline) {
            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_no),
                contentDescription = stringResource(R.string.decline_request_button_content_description),
                tint = Color.Red
            )
        }
    }
}