package com.example.vms.visitdetails

import com.example.vms.model.Visit

/**
 * Created by mśmiech on 25.08.2023.
 */
data class VisitDetailsState(
    val isLoading: Boolean,
    val visit: Visit,
    val isMoreOptionsShowing: Boolean,
    val isEditButtonShowing: Boolean,
    val isCancelVisitDialogShowing: Boolean,
    val isCancelingFailedSnackbarShowing: Boolean,
)