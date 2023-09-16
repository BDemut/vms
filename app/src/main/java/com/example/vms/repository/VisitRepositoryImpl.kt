package com.example.vms.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.vms.model.Room
import com.example.vms.model.Visit
import com.example.vms.model.asModelVisit
import com.example.vms.repository.api.ApiNewVisit
import com.example.vms.repository.api.VisitsClient
import com.example.vms.repository.paging.RequestsPagingSource
import com.example.vms.repository.paging.VisitsPagingSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class VisitRepositoryImpl(val api: VisitsClient) : VisitRepository {
    private val _visitsChangedEvents: MutableSharedFlow<Unit> = MutableSharedFlow()
    override val visitsChangedEvents: SharedFlow<Unit> = _visitsChangedEvents

    override suspend fun getVisit(id: String): Visit {
        return api.getVisit(id).asModelVisit()
    }

    override fun getVisits() = Pager(
        config = PagingConfig(
            pageSize = 20,
        ),
        pagingSourceFactory = {
            VisitsPagingSource(api)
        }
    ).flow

    override suspend fun addVisit(visit: Visit): Boolean {
        val apiNewVisit = ApiNewVisit(
            visit.title,
            ApiNewVisit.Timeframe(
                visit.start,
                visit.end
            ),
            visit.guests.map { it.email },
            visit.room?.id
        )
        val response = api.addVisit(apiNewVisit)
        if (!response.isSuccessful) {
            Log.e("ApiVisitRepositoryImpl", "addVisit failed: ${response.errorBody()?.string()}")
        }
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
    override fun getRequests() = Pager(
        config = PagingConfig(
            pageSize = 20,
        ),
        pagingSourceFactory = {
            RequestsPagingSource(api)
        }
    ).flow

    override suspend fun acceptRequest(requestId: String): Boolean =
        api.declineRequest(requestId).isSuccessful

    override suspend fun declineRequest(requestId: String): Boolean =
        api.declineRequest(requestId).isSuccessful
    override suspend fun onVisitsChanged() {
        _visitsChangedEvents.emit(Unit)
    }
}