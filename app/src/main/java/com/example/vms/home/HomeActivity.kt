package com.example.vms.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.vms.R
import com.example.vms.auditlog.AuditLogActivity
import com.example.vms.home.requests.RequestsTab
import com.example.vms.home.visits.VisitsTab
import com.example.vms.login.LoginActivity
import com.example.vms.settings.SettingsActivity
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*

class HomeActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisitorManagementSystemTheme {
                HomeScreen(model = homeViewModel)
            }
        }
        homeViewModel.events.onEach { event ->
            when (event) {
                is HomeEvent.NavigateToSettings -> launchSettingsActivity()
                is HomeEvent.NavigateToAuditLog -> launchAuditLogActivity()
                is HomeEvent.NavigateToLogin -> launchLoginActivity()
            }
        }.launchIn(lifecycleScope)
    }

    private fun launchSettingsActivity() = startActivity(Intent(this, SettingsActivity::class.java))

    private fun launchAuditLogActivity() = startActivity(Intent(this, AuditLogActivity::class.java))

    private fun launchLoginActivity() = startActivity(Intent(this, LoginActivity::class.java))
}

@Composable
fun HomeScreen(
    model: HomeViewModel
) {
    val state = model.state.collectAsStateWithLifecycle().value
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { HomeToolbar(scaffoldState) },
        bottomBar = {
            HomeBottomBar(
                currentScreen = state.currentTab,
                onBottomNavigationClicked = { newTab -> model.changeTab(newTab) }
            )
        },
        drawerContent = {
            DrawerContent(onMenuItemClick = { item -> model.menuItemClicked(item) })
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colors.background
        ) {
            when (state.currentTab) {
                Tab.VISITS -> VisitsTab(visits = state.visits)
                Tab.REQUESTS -> RequestsTab(requests = state.requests)
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
fun HomeToolbar(scaffoldState: ScaffoldState) {
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
            text = stringResource(R.string.hello, "Janek"),
            fontWeight = FontWeight.Bold
        )
    }
}