package com.example.vms.editvisit.model

/**
 * Created by mśmiech on 01.09.2023.
 */
data class Room(
    val id: String,
    val name: String,
    val isAvailable: Boolean,
    val isSelected: Boolean
)