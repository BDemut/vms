package com.example.vms.editvisit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vms.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Created by mśmiech on 24.08.2023.
 */

private val visitDateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
private val visitItemTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun DateTimeSection(
    date: LocalDate,
    startTime: LocalTime,
    endTime: LocalTime,
    onDateChange: (LocalDate) -> Unit,
    onStartTimeChange: (LocalTime) -> Unit,
    onEndTimeChange: (LocalTime) -> Unit,
    isStartTimeError: Boolean,
    isEndTimeError: Boolean,
    isPastVisitError: Boolean,
) {
    EditableDate(date, onDateChange, isError = isPastVisitError)
    EditableTime(
        time = startTime,
        onTimeChange = onStartTimeChange,
        isError = isStartTimeError || isPastVisitError
    )
    EditableTime(time = endTime, onTimeChange = onEndTimeChange, isError = isEndTimeError)
    if (isStartTimeError || isEndTimeError) {
        ErrorText(text = stringResource(R.string.visit_start_end_time_error))
    }
    if (isPastVisitError) {
        ErrorText(text = stringResource(R.string.past_visit_error))
    }
}

@Composable
fun EditableDate(date: LocalDate, onDateChange: (LocalDate) -> Unit, isError: Boolean) {
    Row(
        modifier = Modifier.background(if (isError) MaterialTheme.colors.error.copy(alpha = 0.5f) else MaterialTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isError) {
            Icon(
                modifier = Modifier.padding(13.dp),
                imageVector = Icons.Default.Error,
                contentDescription = stringResource(R.string.title_error_icon_content_description),
                tint = MaterialTheme.colors.error
            )
        } else {
            Icon(
                modifier = Modifier.padding(13.dp),
                imageVector = Icons.Default.AccessTime,
                contentDescription = stringResource(R.string.datetime_icon_content_description)
            )
        }
        val datePicker = createDatePicker(LocalContext.current, date) {
            onDateChange(it)
        }
        TextButton(onClick = { datePicker.show() }) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(text = date.format(visitDateFormatter).capitalize(Locale.current))
            }
        }
    }
}

@Composable
fun EditableTime(time: LocalTime, onTimeChange: (LocalTime) -> Unit, isError: Boolean) {
    val startTimePicker = createTimePicker(LocalContext.current, time) {
        onTimeChange(it)
    }
    Row(
        modifier = Modifier.background(if (isError) MaterialTheme.colors.error.copy(alpha = 0.5f) else MaterialTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isError) {
            Icon(
                modifier = Modifier.padding(13.dp),
                imageVector = Icons.Default.Error,
                contentDescription = stringResource(R.string.title_error_icon_content_description),
                tint = MaterialTheme.colors.error
            )
        } else {
            Spacer(modifier = Modifier.width(50.dp))
        }
        TextButton(
            onClick = { startTimePicker.show() }
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = time.format(visitItemTimeFormatter),
                )
            }
        }
    }
}

@Composable
fun ErrorText(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp),
        color = MaterialTheme.colors.error
    )
}

@Preview(
    showBackground = true
)
@Composable
fun PreviewDateTimeSection() {
    Column {
        DateTimeSection(
            date = LocalDate.now(),
            startTime = LocalTime.now(),
            endTime = LocalTime.now().plusHours(1),
            onDateChange = {},
            onStartTimeChange = {},
            onEndTimeChange = {},
            isStartTimeError = true,
            isEndTimeError = true,
            isPastVisitError = true
        )
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
    datePicker.datePicker.minDate = System.currentTimeMillis()
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