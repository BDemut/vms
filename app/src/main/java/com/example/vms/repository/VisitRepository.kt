package com.example.vms.repository

import com.example.vms.model.Visit


/**
 * Created by mśmiech on 25.08.2023.
 */
interface VisitRepository {
    suspend fun getVisit(id: String): Visit
    suspend fun getVisits(): List<Visit>
    suspend fun addVisit(visit: Visit)
    suspend fun editVisit(visit: Visit)
}