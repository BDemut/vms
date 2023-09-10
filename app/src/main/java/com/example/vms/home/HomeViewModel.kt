package com.example.vms.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.vms.home.requests.testRequests
import com.example.vms.home.visits.Visit
import com.example.vms.login.Authentication
import com.example.vms.repository.VisitRepository
import com.example.vms.user.User
import com.example.vms.userComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class HomeViewModel(
    private val visitRepository: VisitRepository,
    private val authentication: Authentication,
    signInUser: User
) : ViewModel() {
    val state = MutableStateFlow(
        HomeState(
            currentTab = Tab.VISITS,
            visits = emptyFlow(),
            requests = testRequests,
            isLogoutDialogShowing = false,
            dataState = DataState.LOADING,
            signInUserName = signInUser.email,
        )
    )
    private val _events: MutableSharedFlow<HomeEvent> = MutableSharedFlow()
    val events: SharedFlow<HomeEvent> = _events

    fun changeTab(newTab: Tab) {
        state.update {
            it.copy(currentTab = newTab)
        }
    }

    fun refreshData() {
        state.update {
            when (state.value.currentTab) {
                Tab.VISITS -> it.copy(visits = getVisits(), dataState = DataState.CONTENT)
                Tab.REQUESTS -> it.copy(
                    requests = testRequests,
                    dataState = DataState.CONTENT
                ) //TODO
            }
        }
    }

    private fun getVisits(): Flow<PagingData<Visit>> =
        visitRepository.getVisits()
            .cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map {
                    Visit(it.id, it.title, it.start, it.end, it.isCancelled)
                }
            }

    fun menuItemClicked(item: MenuItemType) {
        viewModelScope.launch {
            when (item) {
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

    fun onRequestClicked(visitId: String) {
        viewModelScope.launch {
            _events.emit(HomeEvent.NavigateToRequestDetails(visitId))
        }
    }

    class Factory : ViewModelProvider.Factory {
        @Inject
        lateinit var visitRepository: VisitRepository

        @Inject
        @Named("signInUser")
        lateinit var signInUser: User

        @Inject
        lateinit var authentication: Authentication

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            val application =
                checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            application.userComponent().inject(this)
            return HomeViewModel(
                visitRepository = visitRepository,
                authentication = authentication,
                signInUser = signInUser
            ) as T
        }
    }
}