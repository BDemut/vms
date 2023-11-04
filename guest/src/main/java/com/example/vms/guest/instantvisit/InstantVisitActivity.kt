package com.example.vms.guest.instantvisit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.vms.R
import com.example.vms.guest.instantvisit.ui.DurationChipGroup
import com.example.vms.guest.instantvisit.ui.DefaultHostSection
import com.example.vms.guest.instantvisit.ui.InstantVisitTextField
import com.example.vms.guest.summary.SummaryActivity
import com.example.vms.guest.summary.SummaryEntryType
import com.example.vms.guest.ui.theme.VisitorManagementSystemTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class InstantVisitActivity : ComponentActivity() {

    private val viewModel: InstantVisitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = viewModel.state.collectAsStateWithLifecycle().value
            val scaffoldState = rememberScaffoldState()
            VisitorManagementSystemTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        scaffoldState = scaffoldState
                    ) {
                        Content(
                            state = state,
                            onNameChanged = viewModel::onNameChanged,
                            onEmailChanged = viewModel::onEmailChanged,
                            onVisitTitleChanged = viewModel::onVisitTitleChanged,
                            onHostEmailChanged = viewModel::onHostEmailChanged,
                            onDurationSelected = viewModel::onDurationSelected,
                            onDefaultHostChecked = viewModel::onDefaultHostCheck,
                            onVisitSubmitted = viewModel::onVisitSubmitted,
                        )
                    }
                }
            }
            LaunchedEffect(Unit) {
                viewModel.events
                    .onEach {
                        when (it) {
                            is InstantVisitEvent.GoToSummary -> {
                                finishAndRemoveTask()
                                launchSummaryActivity()
                            }
                            is InstantVisitEvent.ShowErrorSnackbar -> {
                                scaffoldState.snackbarHostState.showSnackbar(getString(it.message))
                            }
                        }
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun launchSummaryActivity() {
        startActivity(SummaryActivity.createIntent(this, SummaryEntryType.INSTANT_VISIT_REQUESTED))
    }
}

@Composable
fun Content(
    state: InstantVisitState,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onVisitTitleChanged: (String) -> Unit,
    onHostEmailChanged: (String) -> Unit,
    onDurationSelected: (Duration) -> Unit,
    onDefaultHostChecked: (Boolean) -> Unit,
    onVisitSubmitted: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InstantVisitTextField(
            value = state.visit.name,
            onValueChange = onNameChanged,
            placeholder = stringResource(R.string.name_placeholder)
        )
        InstantVisitTextField(
            value = state.visit.email,
            onValueChange = onEmailChanged,
            placeholder = stringResource(R.string.email_placeholder),
        )
        InstantVisitTextField(
            value = state.visit.title,
            onValueChange = onVisitTitleChanged,
            placeholder = stringResource(R.string.visit_title_placeholder)
        )
        DefaultHostSection(
            isSelected = state.defaultHost,
            onChecked = onDefaultHostChecked
        )
        InstantVisitTextField(
            value = state.visit.hostEmail,
            onValueChange = onHostEmailChanged,
            placeholder = stringResource(R.string.host_email_placeholder),
            enabled = !state.defaultHost
        )
        DurationChipGroup(
            modifier = Modifier.width(CONTENT_WIDTH),
            selectedDuration = state.visit.duration,
            onDurationSelected = { onDurationSelected(it) }
        )
        Button(
            onClick = onVisitSubmitted
        ) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = stringResource(R.string.submit_button_labebl).uppercase()
            )
        }
    }
}

val CONTENT_WIDTH = 280.dp