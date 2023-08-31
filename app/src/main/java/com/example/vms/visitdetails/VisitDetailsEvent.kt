package com.example.vms.visitdetails

/**
 * Created by mśmiech on 31.08.2023.
 */
sealed class VisitDetailsEvent {
    object Finish : VisitDetailsEvent()
    class NavigateToEditVisit(val visitId: String) : VisitDetailsEvent()
}