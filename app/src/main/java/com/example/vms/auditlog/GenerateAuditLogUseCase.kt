package com.example.vms.auditlog

import com.example.vms.repository.api.Client
import com.example.vms.repository.api.RequestAuditLogBody
import java.time.LocalDateTime

/**
 * Created by mśmiech on 12.09.2023.
 */
class GenerateAuditLogUseCase(private val api: Client) {
    suspend operator fun invoke(startDateTime: LocalDateTime, endDateTime: LocalDateTime): Boolean {
        val requestBody = RequestAuditLogBody(startDateTime, endDateTime)
        val response = api.requestAuditLog(requestBody)
        return response.isSuccessful
    }
}