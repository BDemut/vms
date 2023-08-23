package com.example.vms.login

/**
 * Created by mśmiech on 20.08.2023.
 */
data class LoginState(
    val isLoading: Boolean,
    val displayValidErrors: Boolean,
    val username: String,
    val isUsernameValid: Boolean,
    val password: String,
    val isPasswordValid: Boolean
) {
    val isValid: Boolean
        get() = isUsernameValid && isPasswordValid
}