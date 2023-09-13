package com.example.vms.auditlog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate
import java.time.LocalTime

/**
 * Created by mśmiech on 12.09.2023.
 */


@Composable
fun AuditLogContent(
    modifier: Modifier = Modifier,
    state: AuditLogState,
    onStartDateChange: (LocalDate) -> Unit = {},
    onStartTimeChange: (LocalTime) -> Unit = {},
    onEndDateChange: (LocalDate) -> Unit = {},
    onEndTimeChange: (LocalTime) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        DateTimeSection(
            startDate = state.startDate,
            startTime = state.startTime,
            endDate = state.endDate,
            endTime = state.endTime,
            onStartDateChange = onStartDateChange,
            onStartTimeChange = onStartTimeChange,
            onEndDateChange = onEndDateChange,
            onEndTimeChange = onEndTimeChange,
        )
        Divider(modifier = Modifier.fillMaxWidth())
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAuditLogContent() {
    AuditLogContent(
        state = AuditLogState(
            isProcessing = false,
            startDate = LocalDate.now().minusDays(1),
            startTime = LocalTime.now(),
            endDate = LocalDate.now(),
            endTime = LocalTime.now(),
            isGenerationFailedSnackbarShowing = false,
            isGenerationSucceedDialogShowing = false,
            signInUserEmail = ""
        )
    )
}