package com.example.vms.auditlog

/**
 * Created by mśmiech on 12.09.2023.
 */

sealed class AuditLogEvent {
    object Finish : AuditLogEvent()
}