package com.example.vms.home

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.vms.R

@Composable
fun HomeBottomBar(
    currentScreen: Tab,
    onBottomNavigationClicked: (Tab) -> Unit
) {
    BottomNavigation {
        BottomNavigationItem(
            selected = currentScreen == Tab.VISITS,
            onClick = { onBottomNavigationClicked(Tab.VISITS) },
            icon = { Text(stringResource(R.string.visits)) },
            alwaysShowLabel = true
        )
        BottomNavigationItem(
            selected = currentScreen == Tab.REQUESTS,
            onClick = { onBottomNavigationClicked(Tab.REQUESTS) },
            icon = { Text(stringResource(R.string.requests)) },
            alwaysShowLabel = true
        )
    }
}