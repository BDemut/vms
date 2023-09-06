package com.example.vms.home.requests

import androidx.annotation.StringRes
import com.example.vms.R

data class Request(
    val id: String,
    val type: RequestType,
    val visitName: String
)

enum class RequestType(@StringRes val description: Int) {
    INSTANT_VISIT(R.string.instant_visit),
    HOST_CHANGE(R.string.host_change),
    // ...
}
