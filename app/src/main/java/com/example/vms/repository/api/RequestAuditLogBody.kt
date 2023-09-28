package com.example.vms.repository.api

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

/**
 * Created by mśmiech on 28.09.2023.
 */
@JsonClass(generateAdapter = true)
data class RequestAuditLogBody(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
)