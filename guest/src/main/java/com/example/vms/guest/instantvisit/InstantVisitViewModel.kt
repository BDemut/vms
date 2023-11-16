package com.example.vms.guest.instantvisit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vms.R
import com.example.vms.guest.api.apiClient
import com.example.vms.guest.api.model.ApiGuest
import com.example.vms.guest.api.model.ApiRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InstantVisitViewModel : ViewModel() {
    private val _state = MutableStateFlow(
        InstantVisitState(
            visit = InstantVisit(
                name = "",
                email = "",
                title = "",
                hostEmail = DEFAULT_HOST,
                duration = Duration.SHORT
            ),
            defaultHost = true
        )
    )
    val state: StateFlow<InstantVisitState> = _state

    private val _events = MutableSharedFlow<InstantVisitEvent>()
    val events: SharedFlow<InstantVisitEvent> = _events

    fun onNameChanged(name: String) {
        _state.update { it.copy(visit = it.visit.copy(name = name)) }
    }

    fun onEmailChanged(email: String) {
        _state.update { it.copy(visit = it.visit.copy(email = email)) }
    }

    fun onVisitTitleChanged(title: String) {
        _state.update { it.copy(visit = it.visit.copy(title = title)) }
    }

    fun onHostEmailChanged(hostEmail: String) {
        _state.update { it.copy(visit = it.visit.copy(hostEmail = hostEmail)) }
    }

    fun onDurationSelected(duration: Duration) {
        _state.update { it.copy(visit = it.visit.copy(duration = duration)) }
    }

    fun onDefaultHostCheck(checked: Boolean) {
        if (checked) {
            _state.update {
                it.copy(
                    visit = it.visit.copy(hostEmail = DEFAULT_HOST),
                    defaultHost = true
                )
            }
        } else {
            _state.update {
                it.copy(
                    visit = it.visit.copy(hostEmail = ""),
                    defaultHost = false
                )
            }
        }
    }

    fun onVisitSubmitted() {
        viewModelScope.launch {
            if (apiClient.requestVisit(_state.value.visit.asApiRequest()).isSuccessful) {
                _events.emit(InstantVisitEvent.GoToSummary)
            } else {
                _events.emit(InstantVisitEvent.ShowErrorSnackbar(R.string.instant_visit_submit_error))
            }
        }
    }

    private fun InstantVisit.asApiRequest() = ApiRequest(
        duration = duration.value,
        hostEmail = hostEmail,
        guest = ApiGuest(
            name = name,
            email = email
        ),
        title = title
    )
}

const val DEFAULT_HOST = "recepcja"

data class InstantVisitState(
    val visit: InstantVisit,
    val defaultHost: Boolean
)

data class InstantVisit(
    val name: String,
    val email: String,
    val title: String,
    val hostEmail: String,
    val duration: Duration,
)

enum class Duration(val value: Int) {
    SHORT(15),
    MEDIUM(30),
    LONG(60)
}