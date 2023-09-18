package com.example.vms.user

/**
 * Created by mśmiech on 06.08.2023.
 */
data class User(
    val email: String,
    val name: String? = null,
    val isAdmin: Boolean = false
)