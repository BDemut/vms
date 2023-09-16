package com.example.vms.networking.adapters

import com.example.vms.repository.api.ApiRequest
import com.example.vms.repository.api.ApiVisit
import com.example.vms.repository.api.AttendeeType
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.Locale

class VisitStatusAdapter {
    @ToJson
    fun toJson(status: ApiVisit.VisitStatus) = status.name.toLowerCase(Locale.ENGLISH)

    @FromJson
    fun fromJson(status: String) = ApiVisit.VisitStatus.valueOf(status.toUpperCase(Locale.ENGLISH))
}

class AttendeeTypeAdapter {
    @ToJson
    fun toJson(status: AttendeeType) = status.name.toLowerCase(Locale.ENGLISH)

    @FromJson
    fun fromJson(status: String) = AttendeeType.valueOf(status.toUpperCase(Locale.ENGLISH))
}

class RequestStatusAdapter {
    @ToJson
    fun toJson(status: ApiRequest.RequestStatus) = status.name.toLowerCase(Locale.ENGLISH)

    @FromJson
    fun fromJson(status: String) = ApiRequest.RequestStatus.valueOf(status.toUpperCase(Locale.ENGLISH))
}