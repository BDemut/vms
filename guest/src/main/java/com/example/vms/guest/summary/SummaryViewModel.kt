package com.example.vms.guest.summary

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vms.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class SummaryViewModel(type: SummaryEntryType) : ViewModel() {
    private val _state = MutableStateFlow(SummaryState(
        description = type.toSummaryDescription(),
        secondsRemaining = 30
    ))
    val state: StateFlow<SummaryState> = _state

    private val _returnEvent = MutableSharedFlow<Unit>()
    val returnEvent: SharedFlow<Unit> = _returnEvent

    init {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val secondsRemaining = _state.value.secondsRemaining
                if (secondsRemaining == 0) {
                    viewModelScope.launch {
                        _returnEvent.emit(Unit)
                    }
                    break
                } else {
                    _state.update {
                        it.copy(secondsRemaining = secondsRemaining - 1)
                    }
                }
                sleep(1000L)
            }
        }
    }

    fun onReturnHomeClicked() {
        viewModelScope.launch {
            _returnEvent.emit(Unit)
        }
    }

    private fun SummaryEntryType.toSummaryDescription() = when (this) {
        SummaryEntryType.PIN_ENTERED -> R.string.awaiting_host_description
        SummaryEntryType.INSTANT_VISIT_REQUESTED -> R.string.visit_pending_description
    }
}

enum class SummaryEntryType {
    PIN_ENTERED,
    INSTANT_VISIT_REQUESTED
}

data class SummaryState(
    @StringRes val description: Int,
    val secondsRemaining: Int = 30
)