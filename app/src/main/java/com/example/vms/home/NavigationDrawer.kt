package com.example.vms.home

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vms.R

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
    onMenuItemClick: (MenuItemType) -> Unit,
    isAuditLogAvailable: Boolean
) {
    DrawerHeader()
    Column {
        if (isAuditLogAvailable) {
            MenuItem(
                type = MenuItemType.AUDIT_LOG,
                onMenuItemClick = onMenuItemClick
            )

        }
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
        text = stringResource(type.description)
    )
}

enum class MenuItemType(@StringRes val description: Int) {
    AUDIT_LOG(R.string.audit_log_menu_item),
    LOGOUT(R.string.logout_menu_item)
}