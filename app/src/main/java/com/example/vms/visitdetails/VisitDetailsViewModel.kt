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
            showMoreOptions = dummyVisit.host == signInUser,
            showEditButton = dummyVisit.host == signInUser,
        )
    )

    private suspend fun setupVisit(visitId: String) {
        val visit = visitRepository.getVisit(visitId)
        state.update {
            it.copy(
                isLoading = false,
                visit = visit,
                showMoreOptions = visit.host == signInUser,
                showEditButton = visit.host == signInUser
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
        //TODO
    }

    fun onCancelVisitButtonClicked() {
        //TODO
    }

    fun onStart() {
        state.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            setupVisit(visitId)
            state.update { it.copy(isLoading = false) }
        }
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
    Visit("", "", LocalDateTime.now(), LocalDateTime.now(), null, emptyList(), User(""))