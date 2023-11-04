package com.example.vms.guest.api

import com.example.vms.guest.api.model.ApiRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Client {
    @POST("/prod/visit-requests")
    @Headers("accept: application/json")
    suspend fun requestVisit(@Body request: ApiRequest): Response<Unit>
}

val apiClient: Client by lazy {
    retrofit.create(Client::class.java)
}