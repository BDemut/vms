package com.example.vms.guest.instantvisit

import androidx.annotation.StringRes

sealed class InstantVisitEvent {
    object GoToSummary: InstantVisitEvent()
    data class ShowErrorSnackbar(@StringRes val message: Int): InstantVisitEvent()
}