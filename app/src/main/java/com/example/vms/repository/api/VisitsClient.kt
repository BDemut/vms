package com.example.vms.repository.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface VisitsClient {
    @GET("/prod/visits")
    @Headers("accept: application/json")
    suspend fun getVisits(): List<ApiVisit>

    @PATCH("/prod/visits/{visitId}/cancelVisit")
    suspend fun cancelVisit(@Path("visitId") visitId: String)

    @GET("/prod/visits/{visitId}")
    @Headers("accept: application/json")
    suspend fun getVisit(@Path("visitId") visitId: String): ApiVisit

    @GET("/prod/rooms")
    @Headers("accept: application/json")
    suspend fun getRooms(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<List<ApiRoom>>
}