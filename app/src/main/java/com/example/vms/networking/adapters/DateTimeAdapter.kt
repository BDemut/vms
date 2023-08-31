package com.example.vms.networking.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeAdapter {
    @ToJson
    fun toJson(date: LocalDateTime) = date.format(DateTimeFormatter.ISO_DATE_TIME) + 'Z'

    @FromJson
    fun fromJson(date: String) = LocalDateTime.parse(date.subSequence(0, date.length-1))
}