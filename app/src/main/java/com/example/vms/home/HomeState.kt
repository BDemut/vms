package com.example.vms.home

import com.example.vms.home.requests.Request
import com.example.vms.home.visits.Visit


data class HomeState(
    val currentTab: Tab,
    val visits: List<Visit>,
    val requests: List<Request>,
    val isLogoutDialogShowing: Boolean,
)