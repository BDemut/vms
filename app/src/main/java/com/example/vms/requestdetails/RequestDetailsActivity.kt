package com.example.vms.requestdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.vms.home.requests.RequestType
import com.example.vms.model.Guest
import com.example.vms.model.Visit
import com.example.vms.ui.InfoDialog
import com.example.vms.ui.LoadingView
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import com.example.vms.user.User
import com.example.vms.visitdetails.VisitDetailsActivity
import com.example.vms.visitdetails.VisitDetailsContent
import com.example.vms.visitdetails.VisitDetailsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime

class RequestDetailsActivity : ComponentActivity() {
    private val requestId: String get() =
        intent.getStringExtra(ARG_REQUEST_ID) ?: throw IllegalStateException("No request id")

    private val viewModel: RequestDetailsViewModel by viewModels(factoryProducer = {
        RequestDetailsViewModel.Factory(requestId)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = viewModel.state.collectAsStateWithLifecycle().value
            val scaffoldState = rememberScaffoldState()
            VisitorManagementSystemTheme {
                Scaffold(
                    scaffoldState = scaffoldState,
                    topBar = {
                        TopBar(
                            requestName = stringResource(id = RequestType.HOST_CHANGE.description),
                            onBackPressed = viewModel::finish
                        )
                    },
                    bottomBar = {
                        RequestDetailsButtons(
                            onAccept = viewModel::onRequestAcceptClicked,
                            onDecline = viewModel::onRequestDeclineClicked
                        )
                    }
                ) { padding ->
                    state.requestedVisit?.let {
                        VisitDetailsContent(
                            modifier = Modifier.padding(padding),
                            visit = state.requestedVisit
                        )
                    } ?: LoadingView(withBackground = true, modifier = Modifier.padding(padding))
                    if (state.infoDialog != null) {
                        InfoDialog(
                            title = state.infoDialog.title,
                            message = state.infoDialog.message,
                            onDismissDialog = viewModel::finish
                        )
                    }
                }
            }
            LaunchedEffect(Unit) {
                viewModel.events.onEach {
                    when (it) {
                        is RequestDetailsEvent.Finish -> finishAndRemoveTask()
                        is RequestDetailsEvent.ShowSnackbar -> scaffoldState.snackbarHostState
                            .showSnackbar(getString(it.message))
                    }
                }.launchIn(lifecycleScope)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.setupRequest()
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
        Guest.InvitationStatus.Accepted,
        null
    ),
    Guest(
        "bartek@test.com",
        Guest.InvitationStatus.Pending,
        "Michał"
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
