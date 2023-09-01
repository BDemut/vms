package com.example.vms.repository

import android.util.Log
import com.example.vms.model.Room
import com.example.vms.model.Visit
import com.example.vms.model.asModelVisit
import com.example.vms.repository.api.VisitsClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ApiVisitRepositoryImpl(val api: VisitsClient) : VisitRepository {
    override suspend fun getVisit(id: String): Visit {
        return api.getVisit(id).asModelVisit()
    }

    override suspend fun getVisits(): List<Visit> = api.getVisits()
        .map { it.asModelVisit() }

    override suspend fun addVisit(visit: Visit) {
        //TODO("Not yet implemented")
    }

    override suspend fun editVisit(visit: Visit) {
        //TODO("Not yet implemented")
    }

    override suspend fun cancelVisit(visitId: String) {
        try {
            api.cancelVisit(visitId)
        } catch (exc: Exception) {
            Log.e("ApiVisitRepositoryImpl", "cancelVisit error", exc)
        }
        //TODO zmienić także status lokalnie w bazie bo backend nie robi tego instant
    }

    override suspend fun getRooms(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): List<Room> {
        try {
            val response = api.getRooms(
                startDate = startDateTime.format(DateTimeFormatter.ISO_DATE_TIME) + 'Z',
                endDate = endDateTime.format(DateTimeFormatter.ISO_DATE_TIME) + 'Z'
            )
            return response.body()?.map { it.asModel() } ?: emptyList()
        } catch (e: Exception) {
            Log.e("ApiVisitRepositoryImpl", "getRooms failed.", e)
            return emptyList()
        }
    }
}