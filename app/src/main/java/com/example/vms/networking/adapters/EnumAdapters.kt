package com.example.vms.networking.adapters

import androidx.compose.ui.text.toUpperCase
import com.example.vms.repository.api.AttendeeType
import com.example.vms.repository.api.VisitStatus
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.Locale

class VisitStatusAdapter {
    @ToJson
    fun toJson(status: VisitStatus) = status.name.toLowerCase(Locale.ENGLISH)

    @FromJson
    fun fromJson(status: String) = VisitStatus.valueOf(status.toUpperCase(Locale.ENGLISH))
}

class AttendeeTypeAdapter {
    @ToJson
    fun toJson(status: AttendeeType) = status.name.toLowerCase(Locale.ENGLISH)

    @FromJson
    fun fromJson(status: String) = AttendeeType.valueOf(status.toUpperCase(Locale.ENGLISH))
}