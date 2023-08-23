package com.example.vms.editvisit

import java.time.LocalDateTime

/**
 * Created by mśmiech on 23.08.2023.
 */
data class Visit(
    val id: Int,
    val title: String,
    val start: LocalDateTime,
    val end: LocalDateTime
)