package com.example.vms.guest.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiRequest(
    val duration: Int,
    val hostEmail: String,
    val guest: ApiGuest,
    val title: String
)

@JsonClass(generateAdapter = true)
data class ApiGuest(
    val name: String,
    val email: String
)