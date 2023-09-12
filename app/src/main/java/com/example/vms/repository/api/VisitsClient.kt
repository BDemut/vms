package com.example.vms.repository.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface VisitsClient {
    @GET("/prod/visits")
    @Headers("accept: application/json")
    suspend fun getVisits(
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 50
    ): GetVisitsResponse

    @PUT("/prod/visits/{visitId}/cancelVisit")
    suspend fun cancelVisit(@Path("visitId") visitId: String): Response<ResponseBody>

    @GET("/prod/visits/{visitId}")
    @Headers("accept: application/json")
    suspend fun getVisit(@Path("visitId") visitId: String): ApiVisit

    @GET("/prod/rooms")
    @Headers("accept: application/json")
    suspend fun getRooms(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("limit") limit: Int = 50
    ): Response<GetRoomsResponse>

    @POST("/prod/visits")
    suspend fun addVisit(@Body createVisitBody: ApiNewVisit): Response<ResponseBody>
}