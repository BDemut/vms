package com.example.vms.visitdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vms.model.Guest
import com.example.vms.model.Room
import com.example.vms.model.Visit
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import com.example.vms.user.User
import java.time.LocalDateTime

class VisitDetailsActivity : ComponentActivity() {
    private lateinit var visitId: String
    private val viewModel: VisitDetailsViewModel by viewModels(factoryProducer = {
        VisitDetailsViewModel.Factory(
            visitId
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        visitId = intent.getStringExtra(ARG_VISIT_ID) ?: throw IllegalStateException("No visit id")
        setContent {
            VisitorManagementSystemTheme {
                VisitDetailsScreen(viewModel)
            }
        }
    }

    companion object {
        const val ARG_VISIT_ID = "visitId"

        fun getLaunchIntent(context: Context, visitId: String): Intent {
            val intent = Intent(context, VisitDetailsActivity::class.java)
            intent.putExtra(ARG_VISIT_ID, visitId)
            return intent
        }
    }
}

@Composable
fun VisitDetailsScreen(viewModel: VisitDetailsViewModel) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
    ) {
        if (state.isLoading) {
            LoadingView()
        } else {
            VisitDetailsContent(
                state = state,
                onDiscardClick = { viewModel.onDiscardButtonClicked() },
                onEditClick = { viewModel.onEditButtonClicked() },
                onChangeHostClick = { viewModel.onChangeHostButtonClicked() },
                onCancelVisitClick = { viewModel.onCancelVisitButtonClicked() },
            )
        }
    }
}

@Composable
fun LoadingView() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LoadingIndicator()
    }
}

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier
            .width(60.dp)
            .height(60.dp)
    )
}

@Composable
fun VisitDetailsContent(
    state: VisitDetailsState,
    onDiscardClick: () -> Unit,
    onEditClick: () -> Unit,
    onChangeHostClick: () -> Unit,
    onCancelVisitClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TopBar(
            onDiscardClick = onDiscardClick,
            onEditClick = onEditClick,
            onChangeHostClick = onChangeHostClick,
            onCancelVisitClick = onCancelVisitClick,
            showMoreOptions = state.showMoreOptions
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(50.dp))
            Column {
                Title(text = state.visit.title)
                VisitDateTime(start = state.visit.start, end = state.visit.end)
            }
        }
        if (state.visit.room != null) {
            LocationSection(room = state.visit.room)
        }
        GuestsSection(guests = state.visit.guests)
    }
}

@Composable
fun Title(text: String) {
    Text(
        text = text,
        fontSize = 24.sp
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewVisitDetailsContent() {
    VisitDetailsContent(
        state = VisitDetailsState(
            isLoading = false,
            visit = testVisit,
            showMoreOptions = true
        ),
        onDiscardClick = {},
        onEditClick = {},
        onChangeHostClick = {},
        onCancelVisitClick = {},
    )
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
        host = User("")
    )