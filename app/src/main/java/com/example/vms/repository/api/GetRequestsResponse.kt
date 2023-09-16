package com.example.vms.repository.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetRequestsResponse(
    val cursor: String?,
    val visitRequests: List<ApiRequest>
)