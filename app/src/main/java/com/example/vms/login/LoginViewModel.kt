package com.example.vms.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.UserState
import com.amazonaws.services.cognitoidentityprovider.model.NotAuthorizedException
import com.example.vms.R
import com.example.vms.appComponent
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by mśmiech on 16.08.2023.
 */
class LoginViewModel(app: Application) : AndroidViewModel(app) {
    private val _events: MutableSharedFlow<LoginEvent> = MutableSharedFlow()
    val events: SharedFlow<LoginEvent> = _events
    val state = MutableStateFlow<LoginState>(
        LoginState(
            false,
            false,
        )
    )
    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()
    val isUsernameValid = username.map { isUsernameValid(it) }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()
    val isPasswordValid = password.map { isPasswordValid(it) }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    @Inject
    lateinit var authentication: Authentication

    init {
        app.appComponent().inject(this)
    }

    fun onLoginButtonClicked() {
        viewModelScope.launch {
            if (!isUsernameValid.value || !isPasswordValid.value) {
                state.update { it.copy(displayValidErrors = true) }
                return@launch
            }
            val username = username.value
            val password = password.value
            state.update { it.copy(isLoading = true) }
//            authentication.signIn("michal.smiech1@gmail.com", "XSW@zaq1")
            val signInResult = authentication.signIn(username, password)
            state.update { it.copy(isLoading = false) }
            handleSignInResult(signInResult)
        }
    }

    fun onStart() {
        viewModelScope.launch {
            state.update { it.copy(isLoading = true) }
            authentication.ensureInit()
            state.update { it.copy(isLoading = false) }
            val currentUserState = AWSMobileClient.getInstance().currentUserState()
            handleUserState(currentUserState.userState)
        }
    }

    fun setUsername(login: String) {
        _username.value = login
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    private fun isUsernameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.isNotBlank()
    }

    private fun handleUserState(userState: UserState) {
        when (userState) {
            UserState.SIGNED_IN -> {
                viewModelScope.launch {
                    _events.emit(LoginEvent.NavigateToHome)
                }
            }
            UserState.SIGNED_OUT -> {
            }
            else -> {
            }
        }
    }

    private fun handleSignInResult(signInResult: Authentication.SignInResult) {
        when(signInResult) {
            is Authentication.SignInResult.Success -> {
                val currentUserState = AWSMobileClient.getInstance().currentUserState()
                handleUserState(currentUserState.userState)
            }
            is Authentication.SignInResult.Error -> {
                viewModelScope.launch {
                    val messageResId = if(signInResult.exception != null
                        && signInResult.exception is NotAuthorizedException
                        && signInResult.exception.errorMessage == "Incorrect username or password.") {
                        R.string.login_error_incorrect_username_or_password
                    } else {
                        R.string.login_error_unknown
                    }
                    _events.emit(LoginEvent.ShowLoginError(messageResId))
                }
            }
        }
    }
}