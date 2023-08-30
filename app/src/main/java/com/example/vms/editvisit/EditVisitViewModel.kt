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
    private var displayNewGuestEmailError = false
    private var displayTitleValidError = false
    val state = MutableStateFlow(
        EditVisitState(
            title = visit?.title ?: "",
            isTitleError = isTitleError(visit?.title ?: ""),
            date = visit?.date ?: LocalDate.now(),
            startTime = visit?.startTime ?: LocalTime.now().plusHours(1).withMinute(0),
            endTime = visit?.endTime ?: LocalTime.now().plusHours(2).withMinute(0),
            room = visit?.room,
            guests = visit?.guests ?: emptyList(),
            isDiscardDialogShowing = false,
            newGuestEmail = "",
            isNewGuestEmailError = isNewGuestEmailError(""),
            showNewGuestEmailClearInputButton = false
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

    private fun isNewGuestEmailError(newGuestEmail: String): Boolean {
        return displayNewGuestEmailError && !validateNewGuestEmail(newGuestEmail) && newGuestEmail.isNotEmpty()
    }

    fun changeEndTime(endTime: LocalTime) {
        state.update { it.copy(endTime = endTime) }
    }

    fun changeTitle(title: String) {
        state.update {
            it.copy(
                title = title,
                isTitleError = isTitleError(title)
            )
        }
    }

    private fun isTitleError(title: String): Boolean {
        return displayTitleValidError && !validateTitle(title)
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
            if (!validateTitle(state.value.title)) {
                displayTitleValidError = true
                this@EditVisitViewModel.state.update { it.copy(isTitleError = isTitleError(it.title)) }
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
        if (!validateNewGuestEmail(state.value.newGuestEmail)) {
            displayNewGuestEmailError = true
            this.state.update { it.copy(isNewGuestEmailError = isNewGuestEmailError(it.newGuestEmail)) }
        } else {
            displayNewGuestEmailError = false
            this.state.update {
                it.copy(
                    guests = LinkedList(it.guests).apply { addFirst(Guest(it.newGuestEmail)) },
                    newGuestEmail = "",
                    isNewGuestEmailError = isNewGuestEmailError(""),
                    showNewGuestEmailClearInputButton = false
                )
            }
        }
    }

    fun onRoomButtonClicked() {
        //TODO
    }

    fun onRemoveGuestButtonClicked(guest: Guest) {
        state.update { it.copy(guests = it.guests.toMutableList().apply { remove(guest) }) }
    }

    fun changeNewGuestEmail(newGuestEmail: String) {
        state.update {
            it.copy(
                newGuestEmail = newGuestEmail,
                isNewGuestEmailError = isNewGuestEmailError(newGuestEmail),
                showNewGuestEmailClearInputButton = newGuestEmail.isNotEmpty()
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