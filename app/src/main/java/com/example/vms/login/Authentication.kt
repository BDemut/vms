package com.example.vms.login

import android.content.Context
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.example.vms.user.User
import com.example.vms.user.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    private val clientMutex = Mutex()
    private var _client: AWSMobileClient? = null

    suspend fun ensureInit() {
        clientMutex.withLock {
            if (_client == null) {
                initAWSMobileClient()
            }
        }
    }

    private fun getClient(): AWSMobileClient {
        if (_client == null) {
            runBlocking {
                ensureInit()
            }
        }
        return _client!!
    }

    fun accessToken(): String {
        return getClient().tokens.idToken.tokenString
    }

    private suspend fun initAWSMobileClient() {
        val client = AWSMobileClient.getInstance()
        val result = client.initialize(context)
        if (result is InitAWSMobileClientResult.Error) {
            Log.e("LoginManager", "AWSMobileClient initialize: error.", result.exception)
        }
        _client = client
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
        val result = client.signIn(username, password)
        if (result is SignInResult.Success) {
            getUser(client)?.let { userManager.startUserSession(it) }
        }
        return result
    }

    private suspend fun AWSMobileClient.signIn(username: String, password: String): SignInResult {
        return suspendCoroutine { continuation ->
            this.signIn(
                username,
                password,
                null,
                object : Callback<com.amazonaws.mobile.client.results.SignInResult> {
                    override fun onResult(result: com.amazonaws.mobile.client.results.SignInResult?) {
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

    fun isSignedIn(): Boolean {
        return getClient().isSignedIn
    }

    private fun isSignedIn(client: AWSMobileClient): Boolean {
        return client.isSignedIn
    }

    private fun getUser(client: AWSMobileClient): User? {
        if (isSignedIn(client)) {
            return User(
                client.username
            )
        }
        return null
    }

    fun currentUserState(): UserStateDetails {
        return getClient().currentUserState()
    }

    sealed class SignInResult {
        object Success : SignInResult()
        class Error(val exception: java.lang.Exception?) : SignInResult()
    }

    sealed class InitAWSMobileClientResult {
        object Success : InitAWSMobileClientResult()
        class Error(val exception: java.lang.Exception?) : InitAWSMobileClientResult()
    }

    fun signOut() {
        getClient().signOut()
        CoroutineScope(Dispatchers.IO).launch {
        }
        userManager.closeUserSession()
    }
}