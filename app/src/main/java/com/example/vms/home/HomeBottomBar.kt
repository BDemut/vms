package com.example.vms.home

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeBottomBar(
    currentScreen: Tab,
    onBottomNavigationClicked: (Tab) -> Unit
) {
    BottomNavigation {
        BottomNavigationItem(
            selected = currentScreen == Tab.VISITS,
            onClick = { onBottomNavigationClicked(Tab.VISITS) },
            icon = { Text("Wizyty") },
            alwaysShowLabel = true
        )
        BottomNavigationItem(
            selected = currentScreen == Tab.REQUESTS,
            onClick = { onBottomNavigationClicked(Tab.REQUESTS) },
            icon = { Text("Zapytania?") },
            alwaysShowLabel = true
        )
    }
}