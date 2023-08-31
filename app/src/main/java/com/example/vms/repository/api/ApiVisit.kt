package com.example.vms.repository.api

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class ApiVisit(
    val id: String,
    val status: VisitStatus,
    val title: String,
    val timeframe: Timeframe,
    val host: ApiHost,
    val guests: List<ApiGuest>,
    val room: ApiRoom
)

@JsonClass(generateAdapter = true)
data class Timeframe(
    val start: LocalDateTime,
    val end: LocalDateTime
)

@JsonClass(generateAdapter = true)
data class ApiHost(
    val email: String,
    val name: String,
    val type: AttendeeType
)

@JsonClass(generateAdapter = true)
data class ApiGuest(
    val email: String,
    val name: String,
    val type: AttendeeType,
    val accepted: Boolean?
)

@JsonClass(generateAdapter = true)
data class ApiRoom(
    val id: String,
    val name: String,
    val isReservedForThisVisit: Boolean
)

enum class VisitStatus {
    CREATED, CANCELED
}

enum class AttendeeType {
    VISITOR, EMPLOYEE
}