package com.example.vms.editvisit.model

import com.example.vms.editvisit.model.Visit.Room
import com.example.vms.user.User
import java.time.LocalDateTime

/**
 * Created by mśmiech on 31.08.2023.
 */
object VisitMapper {
    fun map(visit: com.example.vms.model.Visit): Visit {
        return Visit(
            id = visit.id,
            title = visit.title,
            date = visit.start.toLocalDate(),
            startTime = visit.start.toLocalTime(),
            endTime = visit.end.toLocalTime(),
            room = visit.room?.let { room -> Room(room.id, room.name) },
            guests = visit.guests.map { guest -> Guest(guest.email) }
        )
    }

    fun map(oldVisit: com.example.vms.model.Visit, newVisit: Visit): com.example.vms.model.Visit {
        return com.example.vms.model.Visit(
            id = newVisit.id,
            title = newVisit.title,
            start = LocalDateTime.of(newVisit.date, newVisit.startTime),
            end = LocalDateTime.of(newVisit.date, newVisit.endTime),
            room = newVisit.room?.let { room ->
                com.example.vms.model.Visit.Room(
                    room.id,
                    room.name
                )
            },
            guests = mergedGuests(oldVisit.guests, newVisit.guests),
            host = oldVisit.host,
            isCancelled = oldVisit.isCancelled
        )
    }

    fun map(visit: Visit, host: User): com.example.vms.model.Visit {
        return com.example.vms.model.Visit(
            id = visit.id,
            title = visit.title,
            start = LocalDateTime.of(visit.date, visit.startTime),
            end = LocalDateTime.of(visit.date, visit.endTime),
            room = visit.room?.let { room ->
                com.example.vms.model.Visit.Room(
                    room.id,
                    room.name
                )
            },
            guests = visit.guests.map {
                com.example.vms.model.Guest(
                    it.email,
                    com.example.vms.model.Guest.InvitationStatus.Pending
                )
            },
            host = host,
            isCancelled = false
        )
    }

    private fun mergedGuests(
        old: List<com.example.vms.model.Guest>,
        new: List<Guest>
    ): List<com.example.vms.model.Guest> {
        return new.map { guest ->
            com.example.vms.model.Guest(
                email = guest.email,
                invitationStatus = old.firstOrNull { guest.email == it.email }?.invitationStatus
                    ?: com.example.vms.model.Guest.InvitationStatus.Pending
            )
        }
    }
}