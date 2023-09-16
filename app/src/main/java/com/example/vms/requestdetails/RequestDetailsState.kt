package com.example.vms.requestdetails

import com.example.vms.model.Visit
import com.example.vms.ui.InfoDialog

data class RequestDetailsState(
    val isLoading: Boolean,
    val requestedVisit: Visit?,
    val infoDialog: InfoDialog?,
)