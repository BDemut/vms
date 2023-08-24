package com.example.vms.editvisit

/**
 * Created by mśmiech on 22.08.2023.
 */
sealed class EditVisitEvent {
    object Finish : EditVisitEvent()
}