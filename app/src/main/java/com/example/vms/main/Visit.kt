package com.example.vms.main

import java.time.LocalDateTime

data class Visit(
    val id: Int,
    val title: String,
    val start: LocalDateTime,
    val end: LocalDateTime
)
