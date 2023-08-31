package com.example.vms.home

sealed class HomeEvent {
    object NavigateToSettings: HomeEvent()
    object NavigateToAuditLog: HomeEvent()
    object NavigateToLogin: HomeEvent()
    object NavigateToEditVisit : HomeEvent()
    class NavigateToVisitDetails(val visitId: String) : HomeEvent()
}