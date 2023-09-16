package com.example.vms.home

import androidx.annotation.StringRes

sealed class HomeEvent {
    object NavigateToSettings: HomeEvent()
    object NavigateToAuditLog: HomeEvent()
    object NavigateToLogin: HomeEvent()
    object NavigateToEditVisit : HomeEvent()
    data class NavigateToVisitDetails(val visitId: String) : HomeEvent()
    data class NavigateToRequestDetails(val requestId: String) : HomeEvent()
    data class ShowSnackbar(@StringRes val message: Int) : HomeEvent()
}