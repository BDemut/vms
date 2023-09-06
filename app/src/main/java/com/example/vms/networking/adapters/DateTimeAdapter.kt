package com.example.vms.networking.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeAdapter {
    @ToJson
    fun toJson(date: LocalDateTime) = date.format(DateTimeFormatter.ISO_DATE_TIME)

    @FromJson
    fun fromJson(date: String) = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
}