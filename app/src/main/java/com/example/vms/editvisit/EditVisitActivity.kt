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
                TopBar({
                    viewModel.onDiscardButtonClicked()
                },
                    onSaveClick = {
                        viewModel.onSaveButtonClicked()
                    })
                TitleSection(
                    state.title,
                    onTitleChange = { viewModel.changeTitle(it) },
                    !state.displayTitleValidError || state.isTitleValid
                )
                Divider(modifier = Modifier.fillMaxWidth())
                DateTimeSection(
                    state.date,
                    state.startTime,
                    state.endTime, {
                        viewModel.changeDate(it)
                    }, {
                        viewModel.changeStartTime(it)
                    }, {
                        viewModel.changeEndTime(it)
                    })
                Divider(modifier = Modifier.fillMaxWidth())
                LocationSection(state.room,
                    onClick = {
                        viewModel.onRoomButtonClicked()
                    })
                Divider(modifier = Modifier.fillMaxWidth())
                GuestsSection(
                    state.guests,
                    onAddGuestButtonClicked = { viewModel.onAddGuestButtonClicked() },
                    onRemoveGuestButtonClicked = { viewModel.onRemoveGuestButtonClicked(it) },
                    state.newGuestEmail,
                    { viewModel.changeNewGuestEmail(it) },
                    state.isNewGuestEmailValid,
                    state.displayNewGuestEmailValidError
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