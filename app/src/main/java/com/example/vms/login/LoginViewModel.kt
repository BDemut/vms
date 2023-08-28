package com.example.vms.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobile.client.UserState
import com.amazonaws.services.cognitoidentityprovider.model.NotAuthorizedException
import com.example.vms.R
import com.example.vms.appComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
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
            isLoading = false,
            displayValidErrors = false,
            username = "",
            isUsernameValid = isUsernameValid(""),
            password = "",
            isPasswordValid = isPasswordValid("")
        )
    )

    @Inject
    lateinit var authentication: Authentication

    init {
        app.appComponent().inject(this)
    }

    fun onLoginButtonClicked() {
        CoroutineScope(Dispatchers.IO).launch {
            val state = state.value
            if (!state.isValid) {
                this@LoginViewModel.state.update { it.copy(displayValidErrors = true) }
                return@launch
            }
            this@LoginViewModel.state.update { it.copy(displayValidErrors = true) }
            val signInResult = authentication.signIn(state.username, state.password)
            this@LoginViewModel.state.update { it.copy(displayValidErrors = true) }
            handleSignInResult(signInResult)
        }
    }

    fun onStart() {
        viewModelScope.launch {
            state.update { it.copy(isLoading = true) }
            val currentUserState = authentication.currentUserState()
            state.update { it.copy(isLoading = false) }
            handleUserState(currentUserState.userState)
        }
    }

    fun setUsername(username: String) {
        state.update {
            it.copy(
                username = username,
                isUsernameValid = isUsernameValid(username)
            )
        }
    }

    fun setPassword(password: String) {
        state.update {
            it.copy(
                password = password,
                isPasswordValid = isPasswordValid(password)
            )
        }
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

    private suspend fun handleSignInResult(signInResult: Authentication.SignInResult) {
        when (signInResult) {
            is Authentication.SignInResult.Success -> {
                val currentUserState = authentication.currentUserState()
                handleUserState(currentUserState.userState)
            }

            is Authentication.SignInResult.Error -> {
                viewModelScope.launch {
                    val messageResId = if (signInResult.exception != null
                        && signInResult.exception is NotAuthorizedException
                        && signInResult.exception.errorMessage == "Incorrect username or password."
                    ) {
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