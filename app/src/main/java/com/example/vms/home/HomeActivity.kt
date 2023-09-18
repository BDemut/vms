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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.vms.ui.InfoDialog
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import com.example.vms.user.User
import com.example.vms.user.UserSessionActivity
import com.example.vms.visitdetails.VisitDetailsActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeActivity : UserSessionActivity() {

    private val homeViewModel: HomeViewModel by viewModels(factoryProducer = { HomeViewModel.Factory() })

    override fun onCreateWithUserSession(savedInstanceState: Bundle?) {
        setContent {
            val scaffoldState = rememberScaffoldState()
            VisitorManagementSystemTheme {
                HomeScreen(
                    model = homeViewModel,
                    scaffoldState = scaffoldState
                )
            }
            LaunchedEffect(Unit) {
                homeViewModel.events.onEach { event ->
                    when (event) {
                        is HomeEvent.NavigateToSettings -> {
                            launchSettingsActivity()
                        }
                        is HomeEvent.NavigateToAuditLog -> launchAuditLogActivity()
                        is HomeEvent.NavigateToLogin -> {
                            launchLoginActivity()
                            finish()
                        }
                        is HomeEvent.NavigateToEditVisit -> launchEditVisitActivity()
                        is HomeEvent.NavigateToVisitDetails -> launchVisitDetailsActivity(event.visitId)
                        is HomeEvent.NavigateToRequestDetails -> launchRequestDetailsActivity(event.requestId)
                        is HomeEvent.ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(
                            getString(event.message)
                        )
                    }
                }.launchIn(lifecycleScope)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.refreshVisits()
        homeViewModel.refreshRequests()
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
    model: HomeViewModel,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val state = model.state.collectAsStateWithLifecycle().value
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { HomeToolbar(scaffoldState, state.signInUser) },
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
            HomeContent(
                state = state,
                onVisitClick = model::onVisitClicked,
                onRequestClick = model::onRequestClicked,
                onRefreshVisits = model::refreshVisits,
                onRefreshRequests = model::refreshRequests,
                onRequestAccept = model::onRequestAcceptClicked,
                onRequestDecline = model::onRequestDeclineClicked
            )
            when {
                state.isLogoutDialogShowing -> LogoutDialog(
                    onDismissDialog = model::logoutDialogDismissed,
                    onLogoutClicked = model::logout
                )
                state.infoDialog != null -> InfoDialog(
                    title = state.infoDialog.title,
                    message = state.infoDialog.message,
                    onDismissDialog = model::onInfoDialogDismissed
                )
            }
        }
    }
}

@Composable
fun HomeContent(
    state: HomeState,
    onVisitClick: (String) -> Unit,
    onRequestClick: (String) -> Unit,
    onRefreshVisits: () -> Unit,
    onRefreshRequests: () -> Unit,
    onRequestAccept: (String) -> Unit,
    onRequestDecline: (String) -> Unit,
) {
    when (state.currentTab) {
        Tab.VISITS -> VisitsTab(
            visitsFlow = state.visits,
            onVisitClick = onVisitClick,
            onRefreshData = onRefreshVisits
        )

        Tab.REQUESTS -> RequestsTab(
            requestsFlow = state.requests,
            onRequestClick = onRequestClick,
            onRefreshData = onRefreshRequests,
            onRequestAccept = onRequestAccept,
            onRequestDecline = onRequestDecline
        )
    }
}

@Composable
fun HomeToolbar(scaffoldState: ScaffoldState, signInUser: User) {
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
            text = stringResource(R.string.hello, signInUser.name ?: signInUser.email),
            fontWeight = FontWeight.Bold
        )
    }
}