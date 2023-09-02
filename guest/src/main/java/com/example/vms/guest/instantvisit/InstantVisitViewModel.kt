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
                hostName = "",
                duration = Duration.SHORT
            ),
            hosts = listOf(
                "Andrzej Andrzej",
                "Bartek Bartek",
                "Cezary Cezary",
                "Andrzej Andrzej",
                "Bartek Bartek",
                "Cezary Cezary",
                "Andrzej Andrzej",
                "Bartek Bartek",
                "Cezary Cezary",
                "Andrzej Andrzej",
                "Bartek Bartek",
                "Cezary Cezary",
                "Andrzej Andrzej",
                "Bartek Bartek",
                "Cezary Cezary",
                "Andrzej Andrzej",
                "Bartek Bartek",
                "Cezary Cezary",
                "Andrzej Andrzej",
                "Bartek Bartek",
                "Cezary Cezary",
                "Andrzej Andrzej",
                "Bartek Bartek",
                "Cezary Cezary",
                "Andrzej Andrzej",
                "Bartek Bartek",
                "Cezary Cezary",
                "Andrzej Andrzej",
                "Bartek Bartek",
                "Cezary Cezary",
                "Andrzej Andrzej",
                "Bartek Bartek",
                "Cezary Cezary",
                "Andrzej Andrzej",
                "Bartek Bartek",
                "Cezary Cezary",
                "Andrzej Andrzej",
                "Bartek Bartek",
                "Cezary Cezary",
            )
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

    fun onHostSelected(hostName: String) {
        _state.update { it.copy(visit = it.visit.copy(hostName = hostName)) }
    }

    fun onDurationSelected(duration: Duration) {
        _state.update { it.copy(visit = it.visit.copy(duration = duration)) }
    }

    fun onVisitSubmitted() {
        viewModelScope.launch {
            _submitEvent.emit(Unit)
        }
    }
}

data class InstantVisitState(
    val visit: InstantVisit,
    val hosts: List<String>
)

data class InstantVisit(
    val name: String,
    val phoneNumber: String,
    val visitTitle: String,
    val hostName: String,
    val duration: Duration
)

enum class Duration(val value: String) {
    SHORT("15min"),
    MEDIUM("30min"),
    LONG("1h")
}