package com.example.vms.editvisit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.vms.editvisit.model.Visit
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EditVisitActivity : ComponentActivity() {
    private var visit: Visit? = null
    private val viewModel: EditVisitViewModel by viewModels(factoryProducer = {
        EditVisitViewModel.Factory(
            visit
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisitorManagementSystemTheme {
                EditVisitScreen(viewModel)
            }
        }
        viewModel.events.onEach { event ->
            when (event) {
                is EditVisitEvent.Finish -> finish()
            }
        }.launchIn(lifecycleScope)
    }

    @Composable
    fun EditVisitScreen(viewModel: EditVisitViewModel) {
        val state = viewModel.state.collectAsStateWithLifecycle().value
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TopBar(
                    onDiscardClick = {
                        viewModel.onDiscardButtonClicked()
                    },
                    onSaveClick = {
                        viewModel.onSaveButtonClicked()
                    })
                TitleSection(
                    title = state.title,
                    onTitleChange = { viewModel.changeTitle(it) },
                    isTitleError = state.isTitleError
                )
                Divider(modifier = Modifier.fillMaxWidth())
                DateTimeSection(
                    date = state.date,
                    startTime = state.startTime,
                    endTime = state.endTime,
                    onDateChange = {
                        viewModel.changeDate(it)
                    },
                    onStartTimeChange = {
                        viewModel.changeStartTime(it)
                    },
                    onEndTimeChange = {
                        viewModel.changeEndTime(it)
                    })
                Divider(modifier = Modifier.fillMaxWidth())
                LocationSection(
                    room = state.room,
                    onClick = {
                        viewModel.onRoomButtonClicked()
                    })
                Divider(modifier = Modifier.fillMaxWidth())
                GuestsSection(
                    guests = state.guests,
                    onAddGuestButtonClicked = { viewModel.onAddGuestButtonClicked() },
                    onRemoveGuestButtonClicked = { viewModel.onRemoveGuestButtonClicked(it) },
                    newGuestEmail = state.newGuestEmail,
                    onNewGuestEmailChange = { viewModel.changeNewGuestEmail(it) },
                    isNewGuestEmailError = state.isNewGuestEmailError,
                    showNewGuestEmailClearInputButton = state.showNewGuestEmailClearInputButton
                )
                Divider(modifier = Modifier.fillMaxWidth())
            }
            if (state.isDiscardDialogShowing) {
                DiscardDialog(
                    onConfirm = { viewModel.discard() },
                    onDismiss = { viewModel.discardDialogDismissed() })
            }
        }
    }
}