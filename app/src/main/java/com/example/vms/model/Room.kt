package com.example.vms.model

import com.example.vms.repository.api.ApiVisit

/**
 * Created by mśmiech on 22.08.2023.
 */
data class Room(val id: String, val name: String)

fun ApiVisit.ApiRoom.asModelRoom() = Room(id, name)