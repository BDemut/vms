package com.example.vms.repository.api

import com.squareup.moshi.JsonClass

/**
 * Created by mśmiech on 06.09.2023.
 */
@JsonClass(generateAdapter = true)
data class GetVisitsDto(
    val cursor: String?,
    val visits: List<ApiVisit>
)