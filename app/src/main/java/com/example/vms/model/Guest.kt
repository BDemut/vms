package com.example.vms.model

import com.example.vms.repository.api.ApiGuest

/**
 * Created by mśmiech on 22.08.2023.
 */
data class Guest(val email: String, val invitationStatus: InvitationStatus) {
    enum class InvitationStatus {
        Accepted,
        Declined,
        Pending
    }
}

fun ApiGuest.asModelGuest() = Guest(
    email = email,
    invitationStatus = when (accepted) {
        true -> Guest.InvitationStatus.Accepted
        false -> Guest.InvitationStatus.Declined
        null -> Guest.InvitationStatus.Pending
    }
)