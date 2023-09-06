package com.example.vms.repository

import com.example.vms.model.Room
import com.example.vms.model.Visit
import java.time.LocalDateTime


/**
 * Created by mśmiech on 25.08.2023.
 */
interface VisitRepository {
    suspend fun getVisit(id: String): Visit
    suspend fun getVisits(): List<Visit>
    suspend fun addVisit(visit: Visit)
    suspend fun editVisit(visit: Visit)
    suspend fun cancelVisit(visitId: String)
    suspend fun getRooms(startDateTime: LocalDateTime, endDateTime: LocalDateTime): List<Room>
}