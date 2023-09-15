package com.example.vms.networking

import android.util.Log
import com.example.vms.repository.api.VisitsClient
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

/**
 * Created by mśmiech on 15.09.2023.
 */
class RegisterFCMTokenUseCase(private val api: VisitsClient) {
    suspend operator fun invoke(): Boolean {
        val token = try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            Log.e("RegisterFCMTokenUseCase", "Retrieve FCM token failed", e)
            return false
        }
        Log.d("asd", "token: $token")
//        val response = api.addFCMToken(AddFCMTokenBody(token))
//        return response.isSuccessful
        return true
    }
}