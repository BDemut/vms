package com.example.vms.guest.home

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _pinValue = MutableStateFlow("")
    val pinValue: StateFlow<String> = _pinValue

    private val _events: MutableSharedFlow<HomeEvent> = MutableSharedFlow()
    val events: SharedFlow<HomeEvent> = _events

    fun onPinValueChanged(newValue: String) {
        if (newValue.isDigitsOnly()) {
            _pinValue.update { newValue }
            if (newValue.length == PIN_LENGTH) {
                viewModelScope.launch {
                    _events.emit(HomeEvent.ConfirmPin)
                }
            }
        }
    }

    fun onNoPinButtonClicked() {
        viewModelScope.launch {
            _events.emit(HomeEvent.GoToInstantVisitCreator)
        }
    }
}

sealed class HomeEvent {
    object ConfirmPin : HomeEvent()
    object GoToInstantVisitCreator : HomeEvent()
}