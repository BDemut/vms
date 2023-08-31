package com.example.vms.model.repo

import com.example.vms.model.Visit


/**
 * Created by mśmiech on 25.08.2023.
 */
interface VisitRepository {
    suspend fun getVisit(id: String): Visit
    suspend fun getVisits(): List<Visit>
}