package com.example.vms.visitdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.vms.model.Visit
import com.example.vms.repository.VisitRepository
import com.example.vms.user.User
import com.example.vms.userComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by mśmiech on 24.08.2023.
 */
class VisitDetailsViewModel(
    private val visitId: String,
    private val signInUser: User,
    private val visitRepository: VisitRepository
) : ViewModel() {
    private val _events: MutableSharedFlow<VisitDetailsEvent> = MutableSharedFlow()
    val events: SharedFlow<VisitDetailsEvent> = _events
    val state = MutableStateFlow(
        VisitDetailsState(
            isLoading = true,
            visit = dummyVisit,
            isMoreOptionsShowing = dummyVisit.host.email == signInUser.email,
            isEditButtonShowing = dummyVisit.host.email == signInUser.email,
            isCancelVisitDialogShowing = false,
            isCancelingFailedSnackbarShowing = false
        )
    )

    private suspend fun setupVisit(visitId: String) {
        val visit = visitRepository.getVisit(visitId)
        state.update {
            it.copy(
                isLoading = false,
                visit = visit,
                isMoreOptionsShowing = visit.host.email == signInUser.email && !visit.isCancelled,
                isEditButtonShowing = visit.host.email == signInUser.email && !visit.isCancelled,
            )
        }
    }

    fun onDiscardButtonClicked() {
        viewModelScope.launch {
            _events.emit(VisitDetailsEvent.Finish)
        }
    }

    fun onEditButtonClicked() {
        viewModelScope.launch {
            _events.emit(VisitDetailsEvent.NavigateToEditVisit(visitId))
        }
    }

    fun onChangeHostButtonClicked() {

    }

    fun onCancelVisitButtonClicked() {
        state.update { it.copy(isCancelVisitDialogShowing = true) }
    }

    fun onStart() {
        state.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            setupVisit(visitId)
            state.update { it.copy(isLoading = false) }
        }
    }

    fun onCancelVisitDialogConfirmed() {
        state.update {
            it.copy(
                isLoading = true,
                isCancelVisitDialogShowing = false
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            val succeed = cancelVisit()
            if (succeed) {
                delay(2000)
                setupVisit(visitId)
            } else {
                state.update {
                    it.copy(
                        isLoading = false,
                        isCancelingFailedSnackbarShowing = true
                    )
                }
            }
        }
    }

    fun dismissCancelingFailedSnackbar() {
        state.update { it.copy(isCancelingFailedSnackbarShowing = false) }
    }

    private suspend fun cancelVisit(): Boolean {
        return visitRepository.cancelVisit(visitId)
    }

    fun onCancelVisitDialogDismissed() {
        state.update { it.copy(isCancelVisitDialogShowing = false) }
    }

    class Factory(private val visitId: String) : ViewModelProvider.Factory {
        @Inject
        @Named("signInUser")
        lateinit var signInUser: User

        @Inject
        lateinit var visitRepository: VisitRepository

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            application.userComponent().inject(this)
            return VisitDetailsViewModel(
                visitId,
                signInUser,
                visitRepository
            ) as T
        }
    }
}

private val dummyVisit =
    Visit("", "", LocalDateTime.now(), LocalDateTime.now(), null, emptyList(), User(""), false)