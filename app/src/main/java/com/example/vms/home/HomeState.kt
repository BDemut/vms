package com.example.vms.home

import androidx.annotation.StringRes
import androidx.paging.PagingData
import com.example.vms.home.requests.Request
import com.example.vms.home.visits.Visit
import com.example.vms.ui.InfoDialog
import com.example.vms.user.User
import kotlinx.coroutines.flow.Flow


data class HomeState(
    val currentTab: Tab,
    val visits: Flow<PagingData<Visit>>,
    val requests: Flow<PagingData<Request>>,
    val isLogoutDialogShowing: Boolean,
    val infoDialog: InfoDialog?,
    val signInUser: User,
)