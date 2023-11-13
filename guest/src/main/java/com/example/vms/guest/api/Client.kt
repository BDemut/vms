package com.example.vms.guest.api

import com.example.vms.guest.api.model.ApiPinCode
import com.example.vms.guest.api.model.ApiRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

interface Client {
    @POST("/prod/visit-requests")
    @Headers("accept: application/json")
    suspend fun requestVisit(@Body request: ApiRequest): Response<Unit>

    @PUT("/prod/visits/check-in-visitor")
    @Headers("accept: application/json")
    suspend fun checkIn(@Body pinCode: ApiPinCode): Response<Unit>
}

val apiClient: Client by lazy {
    retrofit.create(Client::class.java)
}