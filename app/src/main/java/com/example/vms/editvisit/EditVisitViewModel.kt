package com.example.vms.editvisit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.vms.editvisit.model.Guest
import com.example.vms.editvisit.model.Visit
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.LinkedList

/**
 * Created by mśmiech on 22.08.2023.
 */
class EditVisitViewModel(app: Application, visit: Visit?) : AndroidViewModel(app) {
    val state = MutableStateFlow(
        EditVisitState(
            visit?.title ?: "",
            validateTitle(visit?.title ?: ""),
            false,
            visit?.start?.toLocalDate() ?: LocalDate.now(),
            visit?.start?.toLocalTime() ?: LocalTime.now().plusHours(1).withMinute(0),
            visit?.end?.toLocalTime() ?: LocalTime.now().plusHours(2).withMinute(0),
            visit?.room,
            visit?.guests ?: emptyList(),
            false,
            "",
            validateNewGuestEmail(""),
            false
        )
    )
    private val _events: MutableSharedFlow<EditVisitEvent> = MutableSharedFlow()
    val events: SharedFlow<EditVisitEvent> = _events

    fun changeDate(date: LocalDate) {
        state.update { it.copy(date = date) }
    }

    fun changeStartTime(startTime: LocalTime) {
        state.update {
            it.copy(
                startTime = startTime,
                endTime = startTime.plusHours(1)
            )
        }
    }

    fun changeEndTime(endTime: LocalTime) {
        state.update { it.copy(endTime = endTime) }
    }

    fun changeTitle(title: String) {
        state.update {
            it.copy(
                title = title,
                isTitleValid = validateTitle(title)
            )
        }
    }

    private fun validateTitle(title: String): Boolean {
        return title.isNotBlank()
    }

    fun discardDialogDismissed() {
        state.update { it.copy(isDiscardDialogShowing = false) }
    }

    fun discard() {
        viewModelScope.launch {
            _events.emit(EditVisitEvent.Finish)
        }
    }

    fun onDiscardButtonClicked() {
        state.update { it.copy(isDiscardDialogShowing = true) }
    }

    fun onSaveButtonClicked() {
        viewModelScope.launch {
            val state = state.value
            if (!state.isTitleValid) {
                this@EditVisitViewModel.state.update { it.copy(displayTitleValidError = true) }
                return@launch
            }
            saveVisit()
            _events.emit(EditVisitEvent.Finish)
        }
    }

    private fun saveVisit() {
        //TODO
    }

    fun onAddGuestButtonClicked() {
        val state = state.value
        if (!state.isNewGuestEmailValid) {
            this.state.update { it.copy(displayNewGuestEmailValidError = state.newGuestEmail.isNotEmpty()) }
        } else {
            this.state.update {
                it.copy(
                    guests = LinkedList(it.guests).apply { addFirst(Guest(it.newGuestEmail)) },
                    newGuestEmail = "",
                    isNewGuestEmailValid = validateNewGuestEmail(""),
                    displayNewGuestEmailValidError = false
                )
            }
        }
    }

    fun onRoomButtonClicked() {

    }

    fun onRemoveGuestButtonClicked(guest: Guest) {
        state.update { it.copy(guests = it.guests.toMutableList().apply { remove(guest) }) }
    }

    fun changeNewGuestEmail(newGuestEmail: String) {
        state.update {
            it.copy(
                newGuestEmail = newGuestEmail,
                isNewGuestEmailValid = validateNewGuestEmail(newGuestEmail)
            )
        }
    }

    private fun validateNewGuestEmail(email: String): Boolean {
        return email.matches(Regex(".+@.+[.].+"))
    }

    class Factory(val visit: Visit?) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            val application = checkNotNull(extras[APPLICATION_KEY])
            return EditVisitViewModel(
                application,
                visit
            ) as T
        }
    }
}