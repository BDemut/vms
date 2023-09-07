package com.example.vms.repository

import android.util.Log
import com.example.vms.model.Room
import com.example.vms.model.Visit
import com.example.vms.model.asModelVisit
import com.example.vms.repository.api.ApiNewVisit
import com.example.vms.repository.api.VisitsClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ApiVisitRepositoryImpl(val api: VisitsClient) : VisitRepository {
    override suspend fun getVisit(id: String): Visit {
        return api.getVisit(id).asModelVisit()
    }

    override suspend fun getVisits(): List<Visit> = api.getVisits()
        .visits
        .map { it.asModelVisit() }

    override suspend fun addVisit(visit: Visit): Boolean {
        val apiNewVisit = ApiNewVisit(
            visit.title,
            ApiNewVisit.Timeframe(
                visit.start,
                visit.end
            ),
            visit.guests.map {
                ApiNewVisit.ApiGuest(
                    it.email,
                    "string" //TODO
                )
            },
            visit.room?.id
        )
        val response = api.addVisit(apiNewVisit)
        return response.isSuccessful
    }

    override suspend fun editVisit(visit: Visit): Boolean {
        //TODO("Not yet implemented")
        return false
    }

    override suspend fun cancelVisit(visitId: String): Boolean {
        val response = api.cancelVisit(visitId)
        return response.isSuccessful
    }

    override suspend fun getRooms(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): List<Room> {
        try {
            val response = api.getRooms(
                startDate = startDateTime.format(DateTimeFormatter.ISO_DATE_TIME),
                endDate = endDateTime.format(DateTimeFormatter.ISO_DATE_TIME)
            )
            return response.body()?.rooms?.map { it.asModel() } ?: emptyList()
        } catch (e: Exception) {
            Log.e("ApiVisitRepositoryImpl", "getRooms failed.", e)
            return emptyList()
        }
    }
}