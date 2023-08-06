package com.example.vms.home

sealed class HomeEvent {
    object NavigateToSettings: HomeEvent()
    object NavigateToAuditLog: HomeEvent()
    object ShowLogoutDialog: HomeEvent()
}