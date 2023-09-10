package com.example.vms.home

import androidx.paging.PagingData
import com.example.vms.home.requests.Request
import com.example.vms.home.visits.Visit
import kotlinx.coroutines.flow.Flow


data class HomeState(
    val currentTab: Tab,
    val visits: Flow<PagingData<Visit>>,
    val requests: List<Request>,
    val isLogoutDialogShowing: Boolean,
    val dataState: DataState,
    val signInUserName: String,
)

enum class DataState {
    CONTENT, LOADING, ERROR
}