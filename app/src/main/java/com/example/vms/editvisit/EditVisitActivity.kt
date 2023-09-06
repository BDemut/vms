package com.example.vms.editvisit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.vms.ui.LoadingView
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class EditVisitActivity : ComponentActivity() {
    private var visitId: String? = null
    private val viewModel: EditVisitViewModel by viewModels(factoryProducer = {
        EditVisitViewModel.Factory(
            visitId
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        visitId = intent.getStringExtra(ARG_VISIT_ID)
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

        onBackPressedDispatcher.addCallback(this) {
            viewModel.onBackPressed()
        }
    }

    companion object {
        const val ARG_VISIT_ID = "visitId"

        fun getLaunchIntent(context: Context, visitId: String? = null): Intent {
            val intent = Intent(context, EditVisitActivity::class.java)
            intent.putExtra(ARG_VISIT_ID, visitId)
            return intent
        }
    }
}

@Composable
fun EditVisitScreen(viewModel: EditVisitViewModel) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
    ) {
        if (state.isLoading) {
            LoadingView()
        } else {
            EditVisitContent(
                state = state,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun EditVisitContent(
    state: EditVisitState,
    viewModel: EditVisitViewModel
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
            onDismiss = { viewModel.discardDialogDismissed() },
            isNewVisit = state.isNewVisit
        )
    }
    if (state.isSaving) {
        LoadingView(withBackground = true)
    }
    val density = LocalDensity.current
    AnimatedVisibility(visible = state.isSelectRoomViewShowing,
        enter = slideInVertically {
            with(density) { 40.dp.roundToPx() }
        } + fadeIn(
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically {
            with(density) { 40.dp.roundToPx() }
        } + fadeOut()
    ) {
        SelectRoomView(viewModel.selectRoomViewModel)
    }
}