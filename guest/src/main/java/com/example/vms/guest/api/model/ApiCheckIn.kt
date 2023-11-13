package com.example.vms.guest.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPinCode(val pinCode: String)