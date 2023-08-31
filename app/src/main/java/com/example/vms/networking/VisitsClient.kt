package com.example.vms.networking

import com.example.vms.model.Visit
import retrofit2.http.GET
import retrofit2.http.Headers

interface VisitsClient {
    @GET("/prod/visits")
    @Headers("accept: application/json")
    suspend fun getVisits() : List<Visit>
}