package com.example.vms.requestdetails

import androidx.annotation.StringRes
import com.example.vms.home.HomeEvent

sealed class RequestDetailsEvent {
    data class ShowSnackbar(@StringRes val message: Int) : RequestDetailsEvent()
    object Finish : RequestDetailsEvent()
}