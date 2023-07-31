package com.example.vms.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vms.R
import com.example.vms.ui.theme.VisitorManagementSystemTheme
import java.util.*

class HomeActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisitorManagementSystemTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen(
    model: HomeViewModel = viewModel()
) {
    val currentTab = model.currentTab.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { HomeToolbar() },
        bottomBar = {
            HomeBottomBar(
                currentScreen = currentTab.value,
                onBottomNavigationClicked = { newScreen -> model.currentTab.value = newScreen }
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colors.background
        ) {
            when (currentTab.value) {
                Tab.VISITS -> VisitsTab(visits = visits, onVisitClick = {})
                Tab.REQUESTS -> Unit //TODO()
            }
        }
    }
}

@Composable
fun HomeToolbar() {
    TopAppBar {
        IconButton(onClick = { /* navigation drawer */ }) {
            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_menu),
                contentDescription = "menu icon"
            )
        }
        Text(
            text = "Hello Janek!",
            fontWeight = FontWeight.Bold
        )
    }
}