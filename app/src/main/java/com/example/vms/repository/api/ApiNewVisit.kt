package com.example.vms.repository.api

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class ApiNewVisit(
    val title: String,
    val timeframe: Timeframe,
    val guestsEmails: List<String>,
    val roomId: String?
) {
    @JsonClass(generateAdapter = true)
    data class ApiRoom(
        val id: String,
        val name: String,
        val isReservedForThisVisit: Boolean
    )

    @JsonClass(generateAdapter = true)
    data class Timeframe(
        val start: LocalDateTime,
        val end: LocalDateTime
    )
}

