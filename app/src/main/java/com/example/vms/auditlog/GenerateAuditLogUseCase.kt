package com.example.vms.auditlog

import kotlinx.coroutines.delay
import java.time.LocalDateTime

/**
 * Created by mśmiech on 12.09.2023.
 */
class GenerateAuditLogUseCase {
    suspend operator fun invoke(startDateTime: LocalDateTime, endDateTime: LocalDateTime): Boolean {
        delay(2000)
        return true
    }
}