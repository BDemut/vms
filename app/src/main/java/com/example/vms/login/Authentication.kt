package com.example.vms.login

import android.content.Context
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobile.client.results.Token
import com.example.vms.user.User
import com.example.vms.user.UserManager
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume
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
                initAWSMobileClient(AWSMobileClient.getInstance())
            }
        }
    }

    suspend fun getClient(): AWSMobileClient {
        ensureInit()
        return AWSMobileClient.getInstance()
    }

    private suspend fun initAWSMobileClient(client: AWSMobileClient): InitAWSMobileClientResult {
        val result = client.initialize(context)
        if (result is InitAWSMobileClientResult.Success) {
            isClientInit = true
            if (isSignedIn(client)) {
                getUser(client)?.let { userManager.startUserSession(it) }
            }
        }
        if (result is InitAWSMobileClientResult.Error) {
            Log.e("LoginManager", "AWSMobileClient initialize: error.", result.exception)
        }
        return result
    }

    private suspend fun AWSMobileClient.initialize(context: Context): InitAWSMobileClientResult {
        return suspendCoroutine { continuation ->
            this.initialize(
                context,
                object : Callback<UserStateDetails?> {
                    override fun onResult(userStateDetails: UserStateDetails?) {
                        continuation.resume(InitAWSMobileClientResult.Success)
                    }

                    override fun onError(e: Exception?) {
                        continuation.resume(InitAWSMobileClientResult.Error(e))
                    }
                }
            )
        }
    }

    suspend fun signIn(username: String, password: String): SignInResult {
        val client = getClient()
        val result = suspendCoroutine<SignInResult> { continuation ->
            client.signIn(
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
            getUser(client)?.let { userManager.startUserSession(it) }
        }
        return result
    }

    private fun isSignedIn(client: AWSMobileClient): Boolean {
        return client.isSignedIn
    }

    private fun getUser(client: AWSMobileClient): User? {
        if (isSignedIn(client)) {
            val userAttributes = client.userAttributes
            return User(
                userAttributes["sub"] as String,
                userAttributes["email"] as String
            )
        }
        return null
    }

    suspend fun getIdToken(): Token {
        return getClient().tokens.idToken
    }

    sealed class SignInResult {
        object Success : SignInResult()
        class Error(val exception: java.lang.Exception?) : SignInResult()
    }

    sealed class InitAWSMobileClientResult {
        object Success : InitAWSMobileClientResult()
        class Error(val exception: java.lang.Exception?) : InitAWSMobileClientResult()
    }

    suspend fun signOut() {
        getClient().signOut()
        userManager.closeUserSession()
    }
}