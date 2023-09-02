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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.vms.R
import com.example.vms.guest.instantvisit.ui.DurationChipGroup
import com.example.vms.guest.instantvisit.ui.HostChangeDropdown
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
            VisitorManagementSystemTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Content(
                        state = state,
                        onNameChanged = { viewModel.onNameChanged(it) },
                        onPhoneNumberChanged = { viewModel.onPhoneNumber(it) },
                        onVisitTitleChanged = { viewModel.onVisitTitleChanged(it) },
                        onHostSelected = { viewModel.onHostSelected(it) },
                        onDurationSelected = { viewModel.onDurationSelected(it) },
                        onVisitSubmitted = { viewModel.onVisitSubmitted() }
                    )
                }
            }
        }
        viewModel.submitEvent
            .onEach {
                finishAndRemoveTask()
                launchSummaryActivity()
            }
            .launchIn(lifecycleScope)
    }

    private fun launchSummaryActivity() {
        startActivity(SummaryActivity.createIntent(this, SummaryEntryType.INSTANT_VISIT_REQUESTED))
    }
}

@Composable
fun Content(
    state: InstantVisitState,
    onNameChanged: (String) -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onVisitTitleChanged: (String) -> Unit,
    onHostSelected: (String) -> Unit,
    onDurationSelected: (Duration) -> Unit,
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
            value = state.visit.phoneNumber,
            onValueChange = onPhoneNumberChanged,
            placeholder = stringResource(R.string.phone_number_placeholder),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        InstantVisitTextField(
            value = state.visit.visitTitle,
            onValueChange = onVisitTitleChanged,
            placeholder = stringResource(R.string.visit_title_placeholder)
        )
        HostChangeDropdown(
            currentHostName = state.visit.hostName,
            availableHosts = state.hosts,
            onHostSelected = onHostSelected
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