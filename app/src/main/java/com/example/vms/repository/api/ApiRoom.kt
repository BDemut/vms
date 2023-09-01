package com.example.vms.repository.api

import com.example.vms.model.Room
import com.squareup.moshi.JsonClass

/**
 * Created by mśmiech on 01.09.2023.
 */
@JsonClass(generateAdapter = true)
data class ApiRoom(
    val id: String,
    val name: String,
    val isAvailable: Boolean
) {
    fun asModel() = Room(id, name, isAvailable)
}