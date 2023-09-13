package com.example.vms.auditlog

import java.time.LocalDate
import java.time.LocalTime

/**
 * Created by mśmiech on 12.09.2023.
 */
data class AuditLogState(
    val isProcessing: Boolean,
    val startDate: LocalDate,
    val startTime: LocalTime,
    val endDate: LocalDate,
    val endTime: LocalTime,
    val isGenerationFailedSnackbarShowing: Boolean,
    val isGenerationSucceedDialogShowing: Boolean,
    val signInUserEmail: String,
)