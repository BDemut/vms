package com.example.vms.editvisit.model

import java.time.LocalDateTime

/**
 * Created by mśmiech on 23.08.2023.
 */
data class Visit(
    val id: Int,
    val title: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val room: Room?,
    val guests: List<Guest>
)