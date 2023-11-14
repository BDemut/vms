package com.example.vms.repository.api

import com.squareup.moshi.JsonClass

/**
 * Created by mśmiech on 14.11.2023.
 */
@JsonClass(generateAdapter = true)
data class ChangeVisitGuestsBody(
    val guestsEmails: List<String>
)