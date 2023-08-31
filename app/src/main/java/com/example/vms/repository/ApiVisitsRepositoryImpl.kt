package com.example.vms.repository

import com.example.vms.model.Visit
import com.example.vms.networking.VisitsClient

class ApiVisitRepositoryImpl(val api: VisitsClient) : VisitRepository {
    override suspend fun getVisit(id: String): Visit {
        TODO("Not yet implemented")
    }

    override suspend fun getVisits(): List<Visit> = api.getVisits()

    override suspend fun addVisit(visit: Visit) {
        TODO("Not yet implemented")
    }

    override suspend fun editVisit(visit: Visit) {
        TODO("Not yet implemented")
    }
}