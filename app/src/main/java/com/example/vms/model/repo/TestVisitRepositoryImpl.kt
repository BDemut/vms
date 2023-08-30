package com.example.vms.model.repo

import com.example.vms.model.Visit
import com.example.vms.user.User
import kotlinx.coroutines.delay
import java.time.LocalDateTime


/**
 * Created by mśmiech on 25.08.2023.
 */
class TestVisitRepositoryImpl : VisitRepository {
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
                    User("", "")
                ),
                Visit(
                    "2",
                    "jakas inna wizyta",
                    LocalDateTime.now().plusHours(3),
                    LocalDateTime.now().plusHours(5),
                    null,
                    emptyList(),
                    User("", "")
                ),
                Visit(
                    "3",
                    "daily",
                    LocalDateTime.now().plusHours(5),
                    LocalDateTime.now().plusHours(6),
                    null,
                    emptyList(),
                    User("", "")
                )
            )
        )
    }

    override suspend fun getVisit(id: String): Visit {
        delay(100)
        return visits.firstOrNull { it.id == id } ?: dummyVisit
    }

    override suspend fun getVisits(): List<Visit> {
        delay(100)
        return visits
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
        User("", "")
    )