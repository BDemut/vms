package com.example.vms.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.vms.R
import com.example.vms.auditlog.AuditLogActivity
import com.example.vms.editvisit.EditVisitActivity
import com.example.vms.home.requests.RequestsTab
import com.example.vms.home.visits.VisitsTab
import com.example.vms.login.LoginActivity
import com.example.vms.requestdetails.RequestDetailsActivity
import com.example.vms.settings.SettingsActivity
import com.example.vms.ui.ErrorMessage
import com.example.vms.ui.LoadingView
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import com.example.vms.user.UserSessionActivity
import com.example.vms.visitdetails.VisitDetailsActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeActivity : UserSessionActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateWithUserSession(savedInstanceState: Bundle?) {
        setContent {
            VisitorManagementSystemTheme {
                HomeScreen(model = homeViewModel)
            }
        }
        homeViewModel.events.onEach { event ->
            when (event) {
                is HomeEvent.NavigateToSettings -> { launchSettingsActivity() }
                is HomeEvent.NavigateToAuditLog -> launchAuditLogActivity()
                is HomeEvent.NavigateToLogin -> {
                    launchLoginActivity()
                    finish()
                }
                is HomeEvent.NavigateToEditVisit -> launchEditVisitActivity()
                is HomeEvent.NavigateToVisitDetails -> launchVisitDetailsActivity(event.visitId)
                is HomeEvent.NavigateToRequestDetails -> launchRequestDetailsActivity(event.requestId)
            }
        }.launchIn(lifecycleScope)
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.getVisits()
    }

    private fun launchSettingsActivity() = startActivity(Intent(this, SettingsActivity::class.java))

    private fun launchAuditLogActivity() = startActivity(Intent(this, AuditLogActivity::class.java))

    private fun launchLoginActivity() = startActivity(Intent(this, LoginActivity::class.java))

    private fun launchEditVisitActivity() =
        startActivity(EditVisitActivity.getLaunchIntent(this))

    private fun launchVisitDetailsActivity(visitId: String) =
        startActivity(VisitDetailsActivity.getLaunchIntent(this, visitId))

    private fun launchRequestDetailsActivity(requestId: String) =
        startActivity(RequestDetailsActivity.getLaunchIntent(this, requestId))
}

@Composable
fun HomeScreen(
    model: HomeViewModel
) {
    val state = model.state.collectAsStateWithLifecycle().value
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { HomeToolbar(scaffoldState, state.signInUserName) },
        bottomBar = {
            HomeBottomBar(
                currentScreen = state.currentTab,
                onBottomNavigationClicked = { newTab -> model.changeTab(newTab) }
            )
        },
        drawerContent = {
            DrawerContent(onMenuItemClick = { item -> model.menuItemClicked(item) })
        },
        floatingActionButton = {
            if (state.currentTab == Tab.VISITS) {
                FloatingActionButton(onClick = { model.onAddVisitClicked() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.title_error_icon_content_description),
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colors.background
        ) {
            when (state.dataState) {
                DataState.CONTENT -> HomeContent(
                    state = state,
                    onVisitClick = model::onVisitClicked,
                    onRequestClick = model::onRequestClicked
                )

                DataState.LOADING -> LoadingView()
                DataState.ERROR -> ErrorMessage(
                    onRetry = { model.getVisits() }
                )
            }
            if (state.isLogoutDialogShowing) {
                LogoutDialog(
                    onDismissDialog = { model.logoutDialogDismissed() },
                    onLogoutClicked = { model.logout() }
                )
            }
        }
    }
}

@Composable
fun HomeContent(
    state: HomeState,
    onVisitClick: (String) -> Unit,
    onRequestClick: (String) -> Unit
) {
    when (state.currentTab) {
        Tab.VISITS -> VisitsTab(visits = state.visits, onVisitClick = onVisitClick)
        Tab.REQUESTS -> RequestsTab(requests = state.requests, onRequestClick = onRequestClick)
    }
}

@Composable
fun HomeToolbar(scaffoldState: ScaffoldState, signInUserName: String) {
    val scope = rememberCoroutineScope()
    TopAppBar {
        IconButton(onClick = {
            scope.launch {
                scaffoldState.drawerState.open()
            }
        }) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(R.string.menu_icon_content_description)
            )
        }
        Text(
            text = stringResource(R.string.hello, signInUserName),
            fontWeight = FontWeight.Bold
        )
    }
}