package com.example.vms.repository

import androidx.paging.PagingData
import com.example.vms.model.Request
import com.example.vms.model.Room
import com.example.vms.model.Visit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import java.time.LocalDateTime


/**
 * Created by mśmiech on 25.08.2023.
 */
interface VisitRepository {
    suspend fun getVisit(id: String): Visit
    fun getVisits(): Flow<PagingData<Visit>>
    suspend fun addVisit(visit: Visit): Boolean
    suspend fun editVisit(visit: Visit): Boolean
    suspend fun cancelVisit(visitId: String): Boolean
    suspend fun getRooms(startDateTime: LocalDateTime, endDateTime: LocalDateTime): List<Room>
    suspend fun getRequest(id: String): Request
    fun getRequests(): Flow<PagingData<Request>>
    suspend fun acceptRequest(requestId: String): Boolean
    suspend fun declineRequest(requestId: String): Boolean
    suspend fun onVisitsChanged()
    val visitsChangedEvents: SharedFlow<Unit>
}