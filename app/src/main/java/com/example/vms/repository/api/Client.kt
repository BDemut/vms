package com.example.vms.repository.api

import com.example.vms.user.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface Client {
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

    @GET("/prod/visit-requests")
    @Headers("accept: application/json")
    suspend fun getRequests(
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 50
    ): GetRequestsResponse

    @GET("/prod/visit-requests/{requestId}")
    @Headers("accept: application/json")
    suspend fun getRequest(@Path("requestId") requestId: String): ApiRequest

    @PUT("/prod/visit-requests/{requestId}/accept")
    @Headers("accept: application/json")
    suspend fun acceptRequest(@Path("requestId") requestId: String): Response<Unit>

    @PUT("/prod/visit-requests/{requestId}/decline")
    @Headers("accept: application/json")
    suspend fun declineRequest(@Path("requestId") requestId: String): Response<Unit>

    @GET("/prod/rooms")
    @Headers("accept: application/json")
    suspend fun getRooms(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("limit") limit: Int = 50
    ): Response<GetRoomsResponse>

    @POST("/prod/visits")
    suspend fun addVisit(@Body createVisitBody: ApiNewVisit): Response<ResponseBody>

    @PUT("/prod/me/addFCMToken")
    suspend fun addFCMToken(@Body body: AddFCMTokenBody): Response<ResponseBody>

    @PUT("/prod/me/addFCMToken")
    suspend fun removeFCMToken(@Body body: RemoveFCMTokenBody): Response<ResponseBody>

    @GET("/prod/me")
    suspend fun userData(): User

    @POST("/prod/me/requestAuditLog")
    suspend fun requestAuditLog(@Body body: RequestAuditLogBody): Response<ResponseBody>

    @PUT("/prod/visits/{visitId}/changeTimeframe")
    suspend fun changeVisitTimeframe(
        @Path("visitId") visitId: String,
        @Body body: ApiVisit.Timeframe
    ): Response<ResponseBody>
}