package com.example.vms.guest.instantvisit

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                phoneNumber = "",
                visitTitle = "",
                hostEmail = DEFAULT_HOST,
                duration = Duration.SHORT
            ),
            defaultHost = true
        )
    )
    val state: StateFlow<InstantVisitState> = _state

    private val _submitEvent = MutableSharedFlow<Unit>()
    val submitEvent: SharedFlow<Unit> = _submitEvent

    fun onNameChanged(name: String) {
        _state.update { it.copy(visit = it.visit.copy(name = name)) }
    }

    fun onPhoneNumber(phoneNumber: String) {
        if (phoneNumber.isDigitsOnly()) {
            _state.update { it.copy(visit = it.visit.copy(phoneNumber = phoneNumber)) }
        }
    }

    fun onVisitTitleChanged(title: String) {
        _state.update { it.copy(visit = it.visit.copy(visitTitle = title)) }
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
            _submitEvent.emit(Unit)
        }
    }
}

const val DEFAULT_HOST = "default"

data class InstantVisitState(
    val visit: InstantVisit,
    val defaultHost: Boolean
)

data class InstantVisit(
    val name: String,
    val phoneNumber: String,
    val visitTitle: String,
    val hostEmail: String,
    val duration: Duration,
)

enum class Duration(val value: String) {
    SHORT("15min"),
    MEDIUM("30min"),
    LONG("1h")
}