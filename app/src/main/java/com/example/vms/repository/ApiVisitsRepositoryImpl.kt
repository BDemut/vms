package com.example.vms.repository

import com.example.vms.model.Visit
import com.example.vms.model.asModelVisit
import com.example.vms.repository.api.VisitsClient

class ApiVisitRepositoryImpl(val api: VisitsClient) : VisitRepository {
    override suspend fun getVisit(id: String): Visit {
        TODO("Not yet implemented")
    }

    override suspend fun getVisits(): List<Visit> = api.getVisits()
        .map { it.asModelVisit() }

    override suspend fun addVisit(visit: Visit) {
        TODO("Not yet implemented")
    }

    override suspend fun editVisit(visit: Visit) {
        TODO("Not yet implemented")
    }
}