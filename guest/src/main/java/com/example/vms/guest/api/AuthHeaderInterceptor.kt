package com.example.vms.guest.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("x-api-key", SUPER_SECRET_API_KEY)
            .build()
        return chain.proceed(request)
    }
}

private val SUPER_SECRET_API_KEY = "Pvz8W07Tai4xxr2tma0eE3VoLWRRPBJE9Sp2GWeP"