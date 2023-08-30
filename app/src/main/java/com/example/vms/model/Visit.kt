package com.example.vms.model

import com.example.vms.user.User
import java.time.LocalDateTime

/**
 * Created by mśmiech on 25.08.2023.
 */
data class Visit(
    val id: String,
    val title: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val room: Room?,
    val guests: List<Guest>,
    val host: User
)