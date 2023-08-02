package com.example.vms.home

import androidx.lifecycle.ViewModel
import com.example.vms.home.requests.Request
import com.example.vms.home.requests.testRequests
import com.example.vms.home.visits.Visit
import com.example.vms.home.visits.testVisits
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel : ViewModel() {

    val state = MutableStateFlow(State(
        currentTab = Tab.VISITS,
        visits = testVisits,
        requests = testRequests
    ))

    fun changeTab(newTab: Tab) {
        state.value = state.value.copy(
            currentTab = newTab
        )
    }

    fun menuItemClicked(item: MenuItemType) {
        //TODO
    }

    data class State(
        val currentTab: Tab,
        val visits: List<Visit>,
        val requests: List<Request>
    )
}