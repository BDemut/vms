package com.example.vms.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vms.home.HomeEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * Created by mśmiech on 16.08.2023.
 */
class LoginViewModel(app: Application) : AndroidViewModel(app) {
    private val _events: MutableSharedFlow<LoginEvent> = MutableSharedFlow()
    val events: SharedFlow<LoginEvent> = _events

    fun onLoginButtonClicked() {
        viewModelScope.launch {
            _events.emit(LoginEvent.NavigateToHome)
        }
    }
}