package com.example.vms.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.vms.R
import com.example.vms.home.requests.Request
import com.example.vms.home.requests.RequestType
import com.example.vms.home.visits.Visit
import com.example.vms.login.Authentication
import com.example.vms.repository.VisitRepository
import com.example.vms.ui.InfoDialog
import com.example.vms.user.User
import com.example.vms.user.UserManager
import com.example.vms.userComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class HomeViewModel(
    private val visitRepository: VisitRepository,
    private val authentication: Authentication,
    private val userManager: UserManager,
    signInUser: User
) : ViewModel() {
    val state = MutableStateFlow(
        HomeState(
            currentTab = Tab.VISITS,
            visits = emptyFlow(),
            requests = emptyFlow(),
            isLogoutDialogShowing = false,
            infoDialog = null,
            signInUser = signInUser,
            isAuditLogAvailable = signInUser.isAdmin == true
        )
    )
    private val _events: MutableSharedFlow<HomeEvent> = MutableSharedFlow()
    val events: SharedFlow<HomeEvent> = _events

    init {
        visitRepository.visitsChangedEvents.onEach {
            refreshVisits()
        }.launchIn(viewModelScope)
    }

    fun changeTab(newTab: Tab) {
        state.update {
            it.copy(currentTab = newTab)
        }
    }

    fun refreshVisits() {
        state.update {
            it.copy(visits = getVisits())
        }
    }

    fun refreshRequests() {
        state.update {
            it.copy(requests = getRequests())
        }
    }

    private fun getVisits(): Flow<PagingData<Visit>> =
        visitRepository.getVisits()
            .cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map {
                    Visit(
                        id = it.id,
                        title = it.title,
                        start = it.start,
                        end = it.end,
                        isCancelled = it.isCancelled
                    )
                }
            }

    private fun getRequests(): Flow<PagingData<Request>> =
        visitRepository.getRequests()
            .cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map {
                    Request(
                        id = it.id,
                        type = RequestType.INSTANT_VISIT,
                        visitName = it.title
                    )
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
            userManager.closeUserSession()
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

    fun onRequestAcceptClicked(requestId: String) {
        viewModelScope.launch {
            visitRepository.acceptRequest(requestId).let { isSuccess ->
                if (isSuccess) {
                    refreshRequests()
                    state.update {
                        it.copy(
                            infoDialog = InfoDialog(
                                title = R.string.request_accept_success_title,
                                message = R.string.request_accept_success_message
                            )
                        )
                    }
                } else {
                    refreshRequests()
                    _events.emit(HomeEvent.ShowSnackbar(R.string.request_accept_decline_failure))
                }
            }
        }
    }

    fun onRequestDeclineClicked(requestId: String) {
        viewModelScope.launch {
            visitRepository.declineRequest(requestId).let { isSuccess ->
                if (isSuccess) {
                    refreshRequests()
                } else {
                    refreshRequests()
                    _events.emit(HomeEvent.ShowSnackbar(R.string.request_accept_decline_failure))
                }
            }
        }
    }

    fun onInfoDialogDismissed() {
        state.update {
            it.copy(infoDialog = null)
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

        @Inject
        lateinit var userManager: UserManager

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
                userManager = userManager,
                signInUser = signInUser
            ) as T
        }
    }
}