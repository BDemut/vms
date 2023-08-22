package com.example.vms.editvisit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.vms.login.LoginEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Created by mśmiech on 22.08.2023.
 */
class EditVisitViewModel(app: Application): AndroidViewModel(app) {
    private val _events: MutableSharedFlow<EditVisitEvent> = MutableSharedFlow()
    val events: SharedFlow<EditVisitEvent> = _events

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

}