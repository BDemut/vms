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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vms.R
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class EditVisitActivity : ComponentActivity() {
    private var visit: Visit? = null
    private val viewModel: EditVisitViewModel by viewModels(factoryProducer = { EditVisitViewModel.Factory(visit) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisitorManagementSystemTheme {
                EditVisitScreen(viewModel)
            }
        }
    }

    private fun createDatePicker(
        context: Context, date: LocalDate, onDatePicked: (LocalDate) -> Unit
    ): DatePickerDialog {
        val year = date.year
        val month = date.monthValue - 1
        val dayOfMonth = date.dayOfMonth
        val datePicker = DatePickerDialog(
            context,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                val pickedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
                onDatePicked(pickedDate)
            },
            year,
            month,
            dayOfMonth
        )
        return datePicker
    }

    private fun createTimePicker(
        context: Context, time: LocalTime, onTimePicked: (LocalTime) -> Unit
    ): TimePickerDialog {
        val hour = time.hour
        val minute = time.minute
        val timePicker = TimePickerDialog(
            context, { _, selectedHour: Int, selectedMinute: Int ->
                val pickedTime = LocalTime.of(selectedHour, selectedMinute)
                onTimePicked(pickedTime)
            }, hour, minute, true
        )
        return timePicker
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
                Topbar({
                    viewModel.onDiscardButtonClicked()
                },
                    onSaveClick = {
                        viewModel.onSaveButtonClicked()
                    })
                Title(
                    state.title
                ) { viewModel.changeTitle(it) }
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
                GuestsSection(state.guests,
                    onAddGuestButtonClicked = { viewModel.onAddGuestButtonClicked() },
                    onRemoveGuestButtonClicked = { viewModel.onRemoveGuestButtonClicked(it) })
                Divider(modifier = Modifier.fillMaxWidth())
            }
            if (state.isDiscardDialogShowing) {
                DiscardDialog(
                    onConfirm = { viewModel.discard() },
                    onDismiss = { viewModel.discardDialogDismissed() })
            }
        }
    }

    @Composable
    fun Topbar(
        onDiscardClick: () -> Unit,
        onSaveClick: () -> Unit
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    onDiscardClick()
                }, modifier = Modifier.padding(8.dp, 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.discard_icon_content_description)
                )
            }
            Button(
                onClick = {
                    onSaveClick()
                }, modifier = Modifier.padding(16.dp, 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.edit_visit_save_button_label)
                )
            }
        }
    }

    @Composable
    fun Title(
        title: String,
        onTitleChange: (String) -> Unit
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(50.dp))
            val fontSize = 24.sp
            NoShapeTextField(
                value = title,
                onValueChange = onTitleChange,
                placeholder = {
                    Text(text = stringResource(R.string.edit_visit_title_placeholder), fontSize = fontSize)
                },
                singleLine = true,
                textStyle = TextStyle.Default.copy(fontSize = fontSize)
            )
        }
    }

    @Composable
    fun DateTimeSection(
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        onDateChange: (LocalDate) -> Unit,
        onStartTimeChange: (LocalTime) -> Unit,
        onEndTimeChange: (LocalTime) -> Unit,
    ) {
        Row {
            Icon(
                modifier = Modifier.padding(13.dp),
                imageVector = Icons.Default.AccessTime,
                contentDescription = stringResource(R.string.datetime_icon_content_description)
            )
            val visitDateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
            val datePicker = createDatePicker(LocalContext.current, date) {
                onDateChange(it)
            }
            TextButton(onClick = { datePicker.show() }) {
                Text(text = date.format(visitDateFormatter).capitalize(Locale.current))
            }
        }
        val visitItemTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val startTimePicker = createTimePicker(LocalContext.current, startTime) {
            onStartTimeChange(it)
        }
        TextButton(
            onClick = { startTimePicker.show() }, modifier = Modifier.padding(start = 50.dp)
        ) {
            Text(
                text = startTime.format(visitItemTimeFormatter)
            )
        }
        val endTimePicker = createTimePicker(LocalContext.current, endTime) {
            onEndTimeChange(it)
        }
        TextButton(
            onClick = { endTimePicker.show() }, modifier = Modifier.padding(start = 50.dp)
        ) {
            Text(
                text = endTime.format(visitItemTimeFormatter)
            )
        }
    }

    @Composable
    fun LocationSection(
        room: Room?,
        onClick: () -> Unit
    ) {
        val roomLabel = room?.name ?: stringResource(id = R.string.edit_visit_room_placeholder)
        Row {
            Icon(
                modifier = Modifier.padding(13.dp),
                imageVector = Icons.Default.LocationOn,
                contentDescription = stringResource(R.string.location_icon_content_description)
            )
            TextButton(onClick = { onClick() }) {
                Text(text = roomLabel)
            }
        }
    }

    @Composable
    fun GuestsSection(
        guests: List<Guest>,
        onAddGuestButtonClicked: () -> Unit,
        onRemoveGuestButtonClicked: (Guest) -> Unit

    ) {
        Row {
            Icon(
                modifier = Modifier.padding(13.dp),
                imageVector = Icons.Default.People,
                contentDescription = stringResource(R.string.guests_icon_content_description)
            )
            Column {
                TextButton(onClick = { onAddGuestButtonClicked() }) {
                    Text(text = stringResource(id = R.string.edit_visit_add_guest_button_label))
                }
                guests.forEach {
                    Guest(guest = it, onRemoveGuestButtonClicked)
                }
            }
        }
    }

    @Composable
    fun Guest(guest: Guest, onRemoveGuestButtonClicked: (Guest) -> Unit) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = guest.email, modifier = Modifier.padding(8.dp, 8.dp)
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.remove_guest_content_description),
                modifier = Modifier.padding(13.dp)
                    .clickable {
                        onRemoveGuestButtonClicked(guest)
                    }
            )
        }
    }
}