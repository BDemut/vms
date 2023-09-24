package com.example.vms.user

import com.squareup.moshi.JsonClass

/**
 * Created by mśmiech on 06.08.2023.
 */
@JsonClass(generateAdapter = true)
data class User(
    val email: String,
    val name: String? = null,
    val isAdmin: Boolean = false
)