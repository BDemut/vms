package com.example.vms.home

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vms.R

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
fun DrawerHeader() {
    Text(
        text = stringResource(id = R.string.app_name),
        fontSize = 24.sp,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
    )
}

@Composable
private fun MenuItem(
    type: MenuItemType,
    onMenuItemClick: (MenuItemType) -> Unit
) {
    Text(
        modifier = Modifier
            .clickable { onMenuItemClick(type) }
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth(),
        text = stringResource(type.description)
    )
}

enum class MenuItemType(@StringRes val description: Int) {
    AUDIT_LOG(R.string.audit_log_menu_item),
    LOGOUT(R.string.logout_menu_item)
}