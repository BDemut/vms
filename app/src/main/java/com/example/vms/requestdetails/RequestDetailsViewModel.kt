package com.example.vms.requestdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.vms.R
import com.example.vms.model.Guest
import com.example.vms.model.Request
import com.example.vms.model.Visit
import com.example.vms.repository.VisitRepository
import com.example.vms.ui.InfoDialog
import com.example.vms.userComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class RequestDetailsViewModel(
    private val requestId: String,
    private val visitRepository: VisitRepository
) : ViewModel() {

    val state = MutableStateFlow(
        RequestDetailsState(
            isLoading = false,
            requestedVisit = null,
            infoDialog = null
        )
    )
    private val _events: MutableSharedFlow<RequestDetailsEvent> = MutableSharedFlow()
    val events: SharedFlow<RequestDetailsEvent> = _events

    fun setupRequest() {
        viewModelScope.launch {
            val request = visitRepository.getRequest(requestId)
            state.update {
                it.copy(
                    requestedVisit = request.createRequestedVisit(),
                )
            }
        }
    }

    fun onRequestAcceptClicked() {
        viewModelScope.launch {
            visitRepository.acceptRequest(requestId).let { isSuccess ->
                if (isSuccess) {
                    state.update {
                        it.copy(
                            infoDialog = InfoDialog(
                                title = R.string.request_accept_success_title,
                                message = R.string.request_accept_success_message
                            )
                        )
                    }
                } else {
                    _events.emit(RequestDetailsEvent.ShowSnackbar(R.string.request_accept_decline_failure))
                }
            }
        }
    }

    fun onRequestDeclineClicked() {
        viewModelScope.launch {
            visitRepository.declineRequest(requestId).let { isSuccess ->
                if (isSuccess) {
                    finish()
                } else {
                    _events.emit(RequestDetailsEvent.ShowSnackbar(R.string.request_accept_decline_failure))
                }
            }
        }
    }

    fun finish() {
        viewModelScope.launch {
            _events.emit(RequestDetailsEvent.Finish)
        }
    }

    private fun Request.createRequestedVisit() = Visit(
        id = "",
        title = title,
        start = LocalDateTime.now(),
        end = LocalDateTime.now().plusMinutes(duration.toLong()),
        room = null,
        guests = listOf(Guest(
            email = guestEmail,
            invitationStatus = Guest.InvitationStatus.Accepted,
            name = "?????"
        )),
        host = host,
        isCancelled = false
    )


    class Factory(private val requestId: String) : ViewModelProvider.Factory {
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
            return RequestDetailsViewModel(
                requestId,
                visitRepository
            ) as T
        }
    }
}