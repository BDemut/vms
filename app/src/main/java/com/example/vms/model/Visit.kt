package com.example.vms.model

import com.example.vms.repository.api.ApiVisit
import com.example.vms.user.User
import java.time.LocalDateTime

/**
 * Created by mśmiech on 25.08.2023.
 */
data class Visit(
    val id: String,
    val title: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val room: Room?,
    val guests: List<Guest>,
    val host: User,
    val isCancelled: Boolean
) {
    data class Room(val id: String, val name: String)
}

fun ApiVisit.asModelVisit() = Visit(
    id = id,
    title = title,
    start = timeframe.start,
    end = timeframe.end,
    room = room?.asModelRoom(),
    guests = guests.map { it.asModelGuest() },
    host = User(host.email),
    isCancelled = this.status == ApiVisit.VisitStatus.CANCELED
)