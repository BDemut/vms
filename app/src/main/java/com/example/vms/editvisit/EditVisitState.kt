package com.example.vms.editvisit

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Created by mśmiech on 23.08.2023.
 */
data class EditVisitState(
    val title: String,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val room: Room?,
    val guests: List<Guest>,
    val isDiscardDialogShowing: Boolean
)