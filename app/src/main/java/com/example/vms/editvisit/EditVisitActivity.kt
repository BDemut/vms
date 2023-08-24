package com.example.vms.editvisit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vms.R
import com.example.vms.editvisit.model.Guest
import com.example.vms.editvisit.model.Room
import com.example.vms.editvisit.model.Visit
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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