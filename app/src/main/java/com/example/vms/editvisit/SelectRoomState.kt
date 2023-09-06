package com.example.vms.editvisit

import com.example.vms.editvisit.model.Room

/**
 * Created by mśmiech on 01.09.2023.
 */
data class SelectRoomState(
    val rooms: List<Room>,
    val isLoading: Boolean
)