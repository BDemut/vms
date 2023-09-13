package com.example.vms.auditlog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Created by mśmiech on 24.08.2023.
 */

private val dateFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun DateTimeSection(
    startDate: LocalDate,
    startTime: LocalTime,
    endDate: LocalDate,
    endTime: LocalTime,
    onStartDateChange: (LocalDate) -> Unit,
    onStartTimeChange: (LocalTime) -> Unit,
    onEndDateChange: (LocalDate) -> Unit,
    onEndTimeChange: (LocalTime) -> Unit,
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(13.dp),
                imageVector = Icons.Default.DateRange,
                contentDescription = "date range"
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                EditableDate(startDate, onStartDateChange)
                EditableTime(startTime, onStartTimeChange)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(50.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                EditableDate(endDate, onEndDateChange)
                EditableTime(endTime, onEndTimeChange)
            }
        }
    }
}

@Composable
fun EditableDate(date: LocalDate, onDateChange: (LocalDate) -> Unit) {
    val datePicker = createDatePicker(LocalContext.current, date) {
        onDateChange(it)
    }
    TextButton(onClick = { datePicker.show() }) {
        Text(text = date.format(dateFormatter).capitalize(Locale.current))
    }
}

@Composable
fun EditableTime(time: LocalTime, onTimeChange: (LocalTime) -> Unit) {
    val startTimePicker = createTimePicker(LocalContext.current, time) {
        onTimeChange(it)
    }
    TextButton(
        onClick = { startTimePicker.show() }
    ) {
        Text(text = time.format(timeFormatter))
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
    datePicker.datePicker.maxDate = System.currentTimeMillis()
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

@Preview(showBackground = true)
@Composable
fun PreviewDateTimeSection() {
    DateTimeSection(
        startDate = LocalDate.now(),
        startTime = LocalTime.now(),
        endDate = LocalDate.now(),
        endTime = LocalTime.now(),
        onStartDateChange = {},
        onStartTimeChange = {},
        onEndDateChange = {},
        onEndTimeChange = {}
    )
}