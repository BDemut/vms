package com.example.vms.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel : ViewModel() {

    val currentTab = MutableStateFlow(Tab.VISITS)
}