package com.example.vms.login

import android.content.Context
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobile.client.results.Token
import com.example.vms.user.User
import com.example.vms.user.UserManager
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


/**
 * Created by mśmiech on 18.08.2023.
 */
class Authentication(
    private val context: Context,
    private val userManager: UserManager
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

    private suspend fun initAWSMobileClient(): InitAWSMobileClientResult {
        val result = suspendCoroutine<InitAWSMobileClientResult> { continuation ->
            AWSMobileClient.getInstance().initialize(
                context,
                object : Callback<UserStateDetails?> {
                    override fun onResult(userStateDetails: UserStateDetails?) {
                        Log.i(
                            "LoginManager",
                            "AWSMobileClient initialize onResult: " + userStateDetails?.userState
                        )
                        isClientInit = true
                        continuation.resume(InitAWSMobileClientResult.Success)
                    }

                    override fun onError(e: Exception?) {
                        Log.e("LoginManager", "AWSMobileClient initialize: error.", e)
                        continuation.resume(InitAWSMobileClientResult.Error(e))
                    }
                }
            )
        }
        if (result is InitAWSMobileClientResult.Success && isSignedIn()) {
            getUser()?.let { userManager.startUserSession(it) }
        }
        return result
    }

    suspend fun signIn(username: String, password: String): SignInResult {
        ensureInit()
        val result = suspendCoroutine<SignInResult> { continuation ->
            AWSMobileClient.getInstance().signIn(
                username,
                password,
                null,
                object : Callback<com.amazonaws.mobile.client.results.SignInResult> {
                    override fun onResult(result: com.amazonaws.mobile.client.results.SignInResult?) {
                        Log.i(
                            "LoginManager",
                            "AWSMobileClient signIn onResult: " + result?.signInState
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
        if (result is SignInResult.Success) {
            getUser()?.let { userManager.startUserSession(it) }
        }
        return result
    }

    private fun isSignedIn(): Boolean {
        return AWSMobileClient.getInstance().isSignedIn
    }

    private fun getUser(): User? {
        if (isSignedIn()) {
            val userAttributes = AWSMobileClient.getInstance().userAttributes
            return User(
                userAttributes["sub"] as String,
                userAttributes["email"] as String
            )
        }
        return null
    }

    suspend fun getIdToken(): Token {
        ensureInit()
        return AWSMobileClient.getInstance().tokens.idToken
    }

    sealed class SignInResult {
        object Success: SignInResult()
        class Error(val exception: java.lang.Exception?): SignInResult()
    }

    sealed class InitAWSMobileClientResult {
        object Success: InitAWSMobileClientResult()
        class Error(val exception: java.lang.Exception?): InitAWSMobileClientResult()
    }

    fun signOut() {
        AWSMobileClient.getInstance().signOut()
        userManager.closeUserSession()
    }
}