package com.example.vms.guest.home

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vms.guest.api.apiClient
import com.example.vms.guest.api.model.ApiPinCode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(initState)
    val state: StateFlow<HomeState> = _state

    private val _events: MutableSharedFlow<HomeEvent> = MutableSharedFlow()
    val events: SharedFlow<HomeEvent> = _events

    fun onPinValueChanged(newValue: String) {
        if (newValue.isDigitsOnly()) {
            _state.update { it.copy(pinValue = newValue) }
            if (newValue.length == PIN_LENGTH) {
                viewModelScope.launch {
                    validatePin(newValue)
                }
            }
        }
    }

    private suspend fun validatePin(pin: String) {
        _state.update { it.copy(isProcessing = true) }
        if (apiClient.checkIn(ApiPinCode(pin)).isSuccessful) {
            _events.emit(HomeEvent.GoToSummary)
        } else {
            _events.emit(HomeEvent.Error)
        }
        _state.update { it.copy(isProcessing = false) }
    }

    fun onNoPinButtonClicked() {
        viewModelScope.launch {
            _events.emit(HomeEvent.GoToInstantVisitCreator)
        }
    }

    fun keyboardShown() {
        _state.update { it.copy(shouldShowKeyboard = false) }
    }

    fun reset() {
        _state.update { initState }
    }
}

private val initState = HomeState(isProcessing = false, pinValue = "", shouldShowKeyboard = true)

data class HomeState(
    val isProcessing: Boolean,
    val pinValue: String,
    val shouldShowKeyboard: Boolean
)

sealed class HomeEvent {
    object GoToSummary : HomeEvent()
    object Error : HomeEvent()
    object GoToInstantVisitCreator : HomeEvent()
}