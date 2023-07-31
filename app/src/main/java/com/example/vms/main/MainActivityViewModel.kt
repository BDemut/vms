package com.example.vms.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivityViewModel : ViewModel() {

    val currentScreen = MutableStateFlow(Screen.VISITS)

    enum class Screen {
        VISITS, REQUESTS
    }
}