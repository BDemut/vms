package com.example.vms.login

import android.content.Context
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


/**
 * Created by mśmiech on 18.08.2023.
 */
class Authentication(
    private val context: Context
) {
    val clientMutex = Mutex()
    var isClientInit = false

    suspend fun ensureInit() {
        clientMutex.withLock {
            if (!isClientInit) {
                initAWSMobileClient()
            }
        }
    }

    private suspend fun initAWSMobileClient() =
        suspendCoroutine<UserStateDetails?> { continuation ->
            AWSMobileClient.getInstance().initialize(
                context,
                object : Callback<UserStateDetails?> {
                    override fun onResult(userStateDetails: UserStateDetails?) {
                        Log.i(
                            "LoginManager",
                            "AWSMobileClient initialize onResult: " + userStateDetails?.userState
                        )
                        isClientInit = true
                        continuation.resume(userStateDetails)
                    }

                    override fun onError(e: Exception?) {
                        Log.e("LoginManager", "AWSMobileClient initialize: error.", e)
                        continuation.resumeWithException(e!!)
                    }
                }
            )
        }

    suspend fun signIn(username: String, password: String) = clientMutex.withLock {
        if (!isClientInit) {
            initAWSMobileClient()
        }
        suspendCoroutine<SignInResult> { continuation ->
            AWSMobileClient.getInstance().signIn(
                username,
                password,
                null,
                object : Callback<com.amazonaws.mobile.client.results.SignInResult> {
                    override fun onResult(result: com.amazonaws.mobile.client.results.SignInResult?) {
                        val token =
                            AWSMobileClient.getInstance().getTokens().getIdToken().getTokenString();
                        Log.i(
                            "LoginManager",
                            "AWSMobileClient signIn onResult: " + result?.signInState + " " + token
                        )
                        continuation.resume(SignInResult.Success)
                    }

                    override fun onError(e: java.lang.Exception?) {
                        Log.e("LoginManager", "AWSMobileClient signIn: error.", e)
                        continuation.resume(SignInResult.Error(e))
                    }
                }
            )
        }
    }

    sealed class SignInResult {
        object Success: SignInResult()
        class Error(val exception: java.lang.Exception?): SignInResult()
    }

    fun signOut() {
        AWSMobileClient.getInstance().signOut()
    }
}