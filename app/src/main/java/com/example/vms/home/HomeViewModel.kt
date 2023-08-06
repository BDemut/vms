package com.example.vms.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vms.home.requests.Request
import com.example.vms.home.requests.testRequests
import com.example.vms.home.visits.Visit
import com.example.vms.home.visits.testVisits
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    val state = MutableStateFlow(State(
        currentTab = Tab.VISITS,
        visits = testVisits,
        requests = testRequests
    ))
    private val _events: MutableSharedFlow<HomeEvent> = MutableSharedFlow()
    val events: SharedFlow<HomeEvent> = _events

    fun changeTab(newTab: Tab) {
        state.value = state.value.copy(
            currentTab = newTab
        )
    }

    fun menuItemClicked(item: MenuItemType) {
        viewModelScope.launch {
            when(item) {
                MenuItemType.SETTINGS -> _events.emit(HomeEvent.NavigateToSettings)
                MenuItemType.AUDIT_LOG -> _events.emit(HomeEvent.NavigateToAuditLog)
                MenuItemType.LOGOUT -> _events.emit(HomeEvent.ShowLogoutDialog)
            }
        }
    }

    data class State(
        val currentTab: Tab,
        val visits: List<Visit>,
        val requests: List<Request>
    )
}