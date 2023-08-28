package com.example.vms.login

/**
 * Created by mśmiech on 18.08.2023.
 */
sealed class LoginEvent {
    object NavigateToHome : LoginEvent()
    class ShowLoginError(val messageResId: Int): LoginEvent()
}