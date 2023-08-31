package com.example.vms.editvisit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.vms.editvisit.model.Guest
import com.example.vms.editvisit.model.Room
import com.example.vms.editvisit.model.Visit
import com.example.vms.model.repo.VisitRepository
import com.example.vms.userComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.LinkedList
import javax.inject.Inject

/**
 * Created by mśmiech on 22.08.2023.
 */
class EditVisitViewModel(
    private val visitId: String?,
    private val visitRepository: VisitRepository
) : ViewModel() {
    private val _events: MutableSharedFlow<EditVisitEvent> = MutableSharedFlow()
    val events: SharedFlow<EditVisitEvent> = _events
    private var displayNewGuestEmailError = false
    private var displayTitleValidError = false
    private val initVisit =
        Visit(
            id = Visit.generateNewId(),
            title = "",
            date = LocalDate.now(),
            startTime = LocalTime.now().plusHours(1).withMinute(0),
            endTime = LocalTime.now().plusHours(2).withMinute(0),
            room = null,
            guests = emptyList()
        )
    val state = MutableStateFlow(
        EditVisitState(
            title = initVisit.title,
            isTitleError = isTitleError(initVisit.title),
            date = initVisit.date,
            startTime = initVisit.startTime,
            endTime = initVisit.endTime,
            room = initVisit.room,
            guests = initVisit.guests,
            isDiscardDialogShowing = false,
            newGuestEmail = "",
            isNewGuestEmailError = isNewGuestEmailError(""),
            showNewGuestEmailClearInputButton = false,
            isLoading = visitId != null,
            isNewVisit = visitId == null,
        )
    )

    init {
        if (visitId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                setupVisit(visitId)
            }
        }
    }

    private suspend fun setupVisit(visitId: String) {
        val visit = visitRepository.getVisit(visitId)
            .let {
                Visit(
                    id = it.id,
                    title = it.title,
                    date = it.start.toLocalDate(),
                    startTime = it.start.toLocalTime(),
                    endTime = it.end.toLocalTime(),
                    room = it.room?.let { room -> Room(room.id, room.name) },
                    guests = it.guests.map { guest -> Guest(guest.email) }
                )
            }
        state.update {
            it.copy(
                isLoading = false,
                title = visit.title,
                isTitleError = isTitleError(visit.title),
                date = visit.date,
                startTime = visit.startTime,
                endTime = visit.endTime,
                room = visit.room,
                guests = visit.guests,
            )
        }
    }

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

    fun onBackPressed() {
        state.update { it.copy(isDiscardDialogShowing = true) }
    }

    class Factory(private val visitId: String?) : ViewModelProvider.Factory {
        @Inject
        lateinit var visitRepository: VisitRepository

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            val application = checkNotNull(extras[APPLICATION_KEY])
            application.userComponent().inject(this)
            return EditVisitViewModel(
                visitId = visitId,
                visitRepository = visitRepository
            ) as T
        }
    }
}

