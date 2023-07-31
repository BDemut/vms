package com.example.vms.home.requests

data class Request(
    val id: Int,
    val type: RequestType,
    val visitName: String
)

enum class RequestType(val description: String) {
    INSTANT_VISIT("Wizyta ad-hoc"),
    HOST_CHANGE("Zmiana hosta"),
    // ...
}
