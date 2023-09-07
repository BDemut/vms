package com.example.vms.editvisit

import com.example.vms.editvisit.model.Guest
import com.example.vms.editvisit.model.Visit.Room
import java.time.LocalDate
import java.time.LocalTime

/**
 * Created by mśmiech on 23.08.2023.
 */
data class EditVisitState(
    val title: String,
    val isTitleError: Boolean,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val isStartTimeError: Boolean,
    val isEndTimeError: Boolean,
    val isPastVisitError: Boolean,
    val room: Room?,
    val guests: List<Guest>,
    val isDiscardDialogShowing: Boolean,
    val newGuestEmail: String,
    val isNewGuestEmailError: Boolean,
    val showNewGuestEmailClearInputButton: Boolean,
    val isLoading: Boolean,
    val isNewVisit: Boolean,
    val isSaving: Boolean,
    val isSelectRoomViewShowing: Boolean,
    val isSavingFailedSnackbarShowing: Boolean
)