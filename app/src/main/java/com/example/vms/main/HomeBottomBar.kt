package com.example.vms.main

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.vms.main.MainActivityViewModel.Screen

@Composable
fun HomeBottomBar(
    currentScreen: Screen,
    onBottomNavigationClicked: (Screen) -> Unit
) {
    BottomNavigation {
        BottomNavigationItem(
            selected = currentScreen == Screen.VISITS,
            onClick = { onBottomNavigationClicked(Screen.VISITS) },
            icon = { Text("Wizyty") },
            alwaysShowLabel = true
        )
        BottomNavigationItem(
            selected = currentScreen == Screen.REQUESTS,
            onClick = { onBottomNavigationClicked(Screen.REQUESTS) },
            icon = { Text("Zapytania?") },
            alwaysShowLabel = true
        )
    }
}