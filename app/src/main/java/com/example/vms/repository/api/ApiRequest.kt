package com.example.vms.repository.api

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class ApiRequest(
    val id: String,
    val status: RequestStatus,
    val title: String,
    val duration: Int,
    val host: ApiAttendee,
    val guest: ApiAttendee,
    val requestDate: LocalDateTime
) {
    enum class RequestStatus {
        REQUESTED, DECLINED, ACCEPTED
    }
}