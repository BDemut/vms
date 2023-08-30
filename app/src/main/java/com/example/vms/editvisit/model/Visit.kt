package com.example.vms.editvisit.model

import java.time.LocalDate
import java.time.LocalTime

/**
 * Created by mśmiech on 23.08.2023.
 */
data class Visit(
    val id: String,
    val title: String,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val room: Room?,
    val guests: List<Guest>
)