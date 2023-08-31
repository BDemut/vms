package com.example.vms.repository.api

import retrofit2.http.GET
import retrofit2.http.Headers

interface VisitsClient {
    @GET("/prod/visits")
    @Headers("accept: application/json")
    suspend fun getVisits() : List<ApiVisit>
}