package com.example.vms.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vms.home.requests.testRequests
import com.example.vms.home.visits.Visit
import com.example.vms.repository.VisitRepository
import com.example.vms.login.Authentication
import com.example.vms.userComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import javax.inject.Inject

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    val state = MutableStateFlow(HomeState(
        currentTab = Tab.VISITS,
        visits = emptyList(),
        requests = testRequests,
        isLogoutDialogShowing = false,
        dataState = DataState.LOADING
    ))
    private val _events: MutableSharedFlow<HomeEvent> = MutableSharedFlow()
    val events: SharedFlow<HomeEvent> = _events

    @Inject
    lateinit var authentication: Authentication

    @Inject
    lateinit var visitRepository: VisitRepository

    init {
        app.userComponent().inject(this)
    }

    fun changeTab(newTab: Tab) {
        state.update {
            it.copy(currentTab = newTab)
        }
    }

    fun getVisits() {
        viewModelScope.launch(Dispatchers.IO) {
            state.update { it.copy(dataState = DataState.LOADING) }
            kotlin.runCatching {
                visitRepository.getVisits()
            }
                .onSuccess {
                    val visits = it.map { Visit(it.id, it.title, it.start, it.end) }
                    state.update { it.copy(visits = visits, dataState = DataState.CONTENT) }
                }
                .onFailure {
                    state.update { it.copy(dataState = DataState.ERROR) }
                }
        }
    }

    fun menuItemClicked(item: MenuItemType) {
        viewModelScope.launch {
            when(item) {
                MenuItemType.SETTINGS -> _events.emit(HomeEvent.NavigateToSettings)
                MenuItemType.AUDIT_LOG -> _events.emit(HomeEvent.NavigateToAuditLog)
                MenuItemType.LOGOUT -> state.update { it.copy(isLogoutDialogShowing = true) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authentication.signOut()
            _events.emit(HomeEvent.NavigateToLogin)
        }
    }

    fun logoutDialogDismissed() {
        state.update { it.copy(isLogoutDialogShowing = false) }
    }

    fun onAddVisitClicked() {
        viewModelScope.launch {
            _events.emit(HomeEvent.NavigateToEditVisit)
        }
    }

    fun onVisitClicked(visitId: String) {
        viewModelScope.launch {
            _events.emit(HomeEvent.NavigateToVisitDetails(visitId))
        }
    }
}