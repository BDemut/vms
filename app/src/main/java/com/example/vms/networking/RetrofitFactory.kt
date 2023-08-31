package com.example.vms.networking

import com.example.vms.networking.adapters.AttendeeTypeAdapter
import com.example.vms.networking.adapters.DateTimeAdapter
import com.example.vms.networking.adapters.VisitStatusAdapter
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitFactory {
    fun createRetrofitInstance(interceptor: AuthHeaderInterceptor) =
        Retrofit.Builder().baseUrl(URL)
        .client(okHttpClient(interceptor))
        .addConverterFactory(MoshiConverterFactory.create(moshi()))
        .build()

    private fun okHttpClient(interceptor: AuthHeaderInterceptor) = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private fun moshi() = Moshi.Builder()
        .add(DateTimeAdapter())
        .add(VisitStatusAdapter())
        .add(AttendeeTypeAdapter())
        .build()
}

private const val URL = "https://a1862n4xz3.execute-api.eu-west-1.amazonaws.com/"