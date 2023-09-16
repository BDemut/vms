package com.example.vms.model

import com.example.vms.repository.api.ApiVisit


/**
 * Created by mśmiech on 22.08.2023.
 */
data class Guest(val email: String, val invitationStatus: InvitationStatus, val name: String?) {
    enum class InvitationStatus {
        Accepted,
        Declined,
        Pending
    }
}

fun ApiVisit.ApiGuest.asModelGuest() = Guest(
    email = email,
    invitationStatus = when (accepted) {
        true -> Guest.InvitationStatus.Accepted
        false -> Guest.InvitationStatus.Declined
        null -> Guest.InvitationStatus.Pending
    },
    name = name
)