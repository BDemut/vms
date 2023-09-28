package com.example.vms.repository.api

import com.squareup.moshi.JsonClass

/**
 * Created by mśmiech on 15.09.2023.
 */
@JsonClass(generateAdapter = true)
data class RemoveFCMTokenBody(val FCMToken: String)