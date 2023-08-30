package com.example.vms.model

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