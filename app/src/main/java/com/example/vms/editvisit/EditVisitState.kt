package com.example.vms.editvisit

import com.example.vms.editvisit.model.Guest
import com.example.vms.editvisit.model.Room
import java.time.LocalDate
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
    val isDiscardDialogShowing: Boolean,
    val newGuestEmail: String,
    val isNewGuestEmailValid: Boolean,
    val displayNewGuestEmailValidError: Boolean
)