package com.example.vms.repository.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiAttendee(
    val email: String,
    val name: String,
    val type: AttendeeType
)

enum class AttendeeType {
    VISITOR, EMPLOYEE
}