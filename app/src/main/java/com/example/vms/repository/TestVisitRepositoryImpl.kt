package com.example.vms.repository

import com.example.vms.model.Visit
import com.example.vms.user.User
import kotlinx.coroutines.delay
import java.time.LocalDateTime


/**
 * Created by mśmiech on 25.08.2023.
 */
class TestVisitRepositoryImpl(
    private val signInUser: User
) : VisitRepository {
    private val visits = mutableListOf<Visit>()

    init {
        visits.addAll(
            listOf(
                Visit(
                    "1",
                    "wizyta prezesa",
                    LocalDateTime.now(),
                    LocalDateTime.now().plusHours(1),
                    null,
                    emptyList(),
                    signInUser,
                    false
                ),
                Visit(
                    "2",
                    "jakas inna wizyta",
                    LocalDateTime.now().plusHours(3),
                    LocalDateTime.now().plusHours(5),
                    null,
                    emptyList(),
                    signInUser,
                    false
                ),
                Visit(
                    "3",
                    "daily",
                    LocalDateTime.now().plusHours(5),
                    LocalDateTime.now().plusHours(6),
                    null,
                    emptyList(),
                    User(""),
                    false
                )
            )
        )
    }

    override suspend fun getVisit(id: String): Visit {
        delay(500)
        return visits.firstOrNull { it.id == id } ?: dummyVisit
    }

    override suspend fun getVisits(): List<Visit> {
        delay(100)
        return visits
    }

    override suspend fun addVisit(visit: Visit) {
        delay(500)
        visits.add(visit)
    }

    override suspend fun editVisit(visit: Visit) {
        delay(500)
        val oldVisit = getVisit(visit.id)
        visits.set(visits.indexOf(oldVisit), visit)
    }

    override suspend fun cancelVisit(visitId: String) {

    }
}

val dummyVisit =
    Visit(
        "",
        "",
        LocalDateTime.now(),
        LocalDateTime.now(),
        null,
        emptyList(),
        User(""),
        false
    )