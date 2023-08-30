package com.example.vms.networking

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitFactory {
    fun createRetrofitInstance(interceptor: AuthHeaderInterceptor) =
        Retrofit.Builder().baseUrl(URL)
        .client(okHttpClient(interceptor))
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private fun okHttpClient(interceptor: AuthHeaderInterceptor) = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
}

private const val URL = "https://a1862n4xz3.execute-api.eu-west-1.amazonaws.com/"