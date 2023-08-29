package com.example.vms.networking

import com.example.vms.login.Authentication
import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor(private val authentication: Authentication) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", authentication.accessToken())
            .build()
        return chain.proceed(request)
    }
}