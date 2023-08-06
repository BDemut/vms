package com.example.vms.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .width(150.dp)
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Placeholder header", fontSize = 24.sp)
    }
}

@Composable
fun DrawerContent(
    onMenuItemClick: (MenuItemType) -> Unit
) {
    DrawerHeader()
    Column {
        MenuItem(
            type = MenuItemType.SETTINGS,
            onMenuItemClick = onMenuItemClick
        )
        MenuItem(
            type = MenuItemType.AUDIT_LOG,
            onMenuItemClick = onMenuItemClick
        )
        MenuItem(
            type = MenuItemType.LOGOUT,
            onMenuItemClick = onMenuItemClick
        )
    }
}

@Composable
private fun MenuItem(
    type: MenuItemType,
    onMenuItemClick: (MenuItemType) -> Unit
) {
    Text(
        modifier = Modifier
            .clickable { onMenuItemClick(type) }
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .fillMaxWidth(),
        text = type.description
    )
}

enum class MenuItemType(val description: String) {
    SETTINGS("Settings"),
    AUDIT_LOG("Audit log"),
    LOGOUT("Logout")
}