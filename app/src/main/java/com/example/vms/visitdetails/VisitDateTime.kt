package com.example.vms.visitdetails

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by mśmiech on 30.08.2023.
 */

private val visitStartDateTimeFormatter = DateTimeFormatter.ofPattern("EEEE dd MMM yyyy • HH:mm-")
private val visitEndDateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun VisitDateTime(start: LocalDateTime, end: LocalDateTime) {
    val dateTime =
        start.format(visitStartDateTimeFormatter).capitalize(Locale.current) + end.format(
            visitEndDateTimeFormatter
        )
    Text(text = dateTime)
}