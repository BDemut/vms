package com.example.vms.model

import com.example.vms.repository.api.ApiRequest
import com.example.vms.user.User

data class Request(
    val id: String,
    val title: String,
    val duration: Int,
    val guestEmail: String,
    val host: User
)

fun ApiRequest.asModelRequest() = Request(
    id = id,
    title = title,
    duration = duration,
    guestEmail = guest.email,
    host = User(host.email)
)