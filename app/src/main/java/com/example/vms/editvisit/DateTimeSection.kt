package com.example.vms.editvisit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.runtime.Composable
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
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(text = date.format(visitDateFormatter).capitalize(Locale.current))
            }
        }
    }
    val visitItemTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val startTimePicker = createTimePicker(LocalContext.current, startTime) {
        onStartTimeChange(it)
    }
    TextButton(
        onClick = { startTimePicker.show() }, modifier = Modifier.padding(start = 50.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = startTime.format(visitItemTimeFormatter),
            )
        }
    }
    val endTimePicker = createTimePicker(LocalContext.current, endTime) {
        onEndTimeChange(it)
    }
    TextButton(
        onClick = { endTimePicker.show() }, modifier = Modifier.padding(start = 50.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(text = endTime.format(visitItemTimeFormatter))
        }
    }
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
            endTime = LocalTime.now(),
            onDateChange = {},
            onStartTimeChange = {},
            onEndTimeChange = {}
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