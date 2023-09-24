package com.example.vms.user

import com.example.vms.repository.api.Client

class UserProvider(val api: Client) {
    suspend fun getUser() = kotlin.runCatching {
        api.userData()
    }.onFailure { it.hashCode() }
        .getOrNull()
}