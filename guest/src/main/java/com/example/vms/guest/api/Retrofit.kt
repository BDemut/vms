package com.example.vms.guest.api

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val retrofit: Retrofit by lazy { retrofit() }

private fun retrofit() = Retrofit.Builder()
    .baseUrl(URL)
    .client(okHttpClient())
    .addConverterFactory(MoshiConverterFactory.create(moshi()))
    .build()

private fun okHttpClient() = OkHttpClient.Builder()
    .addInterceptor(AuthHeaderInterceptor())
    .build()

private fun moshi() = Moshi.Builder()
    .build()

private const val URL = "https://a1862n4xz3.execute-api.eu-west-1.amazonaws.com/"