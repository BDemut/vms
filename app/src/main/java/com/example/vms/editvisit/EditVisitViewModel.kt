package com.example.vms.editvisit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime

/**
 * Created by mśmiech on 22.08.2023.
 */
class EditVisitViewModel(app: Application, visit: Visit?): AndroidViewModel(app) {
    val state = MutableStateFlow(EditVisitState(
        "",
        LocalDate.now(),
        LocalTime.now().plusHours(1).withMinute(0),
        LocalTime.now().plusHours(2).withMinute(0),
        null,
        testGuests,
        false
    ))
    private val _events: MutableSharedFlow<EditVisitEvent> = MutableSharedFlow()
    val events: SharedFlow<EditVisitEvent> = _events

    fun changeDate(date: LocalDate) {
        state.update { it.copy(date = date) }
    }

    fun changeStartTime(startTime: LocalTime) {
        state.update { it.copy(startTime = startTime) }
    }

    fun changeEndTime(endTime: LocalTime) {
        state.update { it.copy(endTime = endTime) }
    }

    fun changeTitle(title: String) {
        state.update { it.copy(title = title) }
    }

    fun discardDialogDismissed() {
        state.update { it.copy(isDiscardDialogShowing = false) }
    }

    fun discard() {
        //TODO
    }

    fun onDiscardButtonClicked() {
        state.update { it.copy(isDiscardDialogShowing = true) }
    }

    fun onSaveButtonClicked() {
        //TODO
    }

    fun onAddGuestButtonClicked() {

    }

    fun onRoomButtonClicked() {

    }

    fun onRemoveGuestButtonClicked(guest: Guest) {
        state.update { it.copy(guests = it.guests.toMutableList().apply { remove(guest) }) }
    }

    class Factory(val visit: Visit?): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            // Get the Application object from extras
            val application = checkNotNull(extras[APPLICATION_KEY])
            // Create a SavedStateHandle for this ViewModel from extras
            val savedStateHandle = extras.createSavedStateHandle()

            return EditVisitViewModel(
                application,
                visit
                ) as T
        }
    }
}

val testGuests = listOf(
    Guest("123", "jan@test.com"),
    Guest("123", "bartek@test.com"),
    Guest("123", "michal@test.com"),
)