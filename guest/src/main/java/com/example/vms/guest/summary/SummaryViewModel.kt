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
    private val _state = MutableStateFlow(type.toInitState())
    val state: StateFlow<SummaryState> = _state

    private val _returnEvent = MutableSharedFlow<Unit>()
    val returnEvent: SharedFlow<Unit> = _returnEvent

    init {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val button = _state.value.backButton as? BackButton.CountDown
                button?.let {
                    if (button.secondsRemaining == 0) {
                        _state.update {
                            it.copy(backButton = BackButton.Enabled)
                        }
                    } else {
                        _state.update {
                            it.copy(
                                backButton = BackButton.CountDown(
                                    button.secondsRemaining - 1
                                )
                            )
                        }
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

    private fun SummaryEntryType.toInitState() = when (this) {
        SummaryEntryType.PIN_ENTERED -> SummaryState(
            R.string.awaiting_host_description,
            BackButton.CountDown(30)
        )

        SummaryEntryType.INSTANT_VISIT_REQUESTED -> SummaryState(
            R.string.visit_pending_description,
            BackButton.Gone
        )
    }
}

enum class SummaryEntryType {
    PIN_ENTERED,
    INSTANT_VISIT_REQUESTED
}

data class SummaryState(
    @StringRes val description: Int,
    val backButton: BackButton
)

sealed class BackButton {
    object Gone : BackButton()
    object Enabled : BackButton()
    data class CountDown(val secondsRemaining: Int) : BackButton()
}