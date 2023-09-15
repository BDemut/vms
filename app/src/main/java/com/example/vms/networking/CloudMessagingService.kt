package com.example.vms.networking

import android.util.Log
import com.example.vms.appComponent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by mśmiech on 15.09.2023.
 */
class CloudMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            if (appComponent().getAuthentication().isSignedIn()) {
                val succeed = appComponent().getRegisterFCMTokenUseCase().invoke()
                Log.d("asd", "RegisterFCMTokenUseCase $succeed")
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isEmpty()) {
            Log.w(
                "CloudMessagingService",
                "FCM data is null (messageId:${remoteMessage.messageId})"
            )
            return
        }

        if (!remoteMessage.data.containsKey(FcmConstants.FIELD_MESSAGE_TYPE)) {
            Log.w(
                "CloudMessagingService",
                "FCM without message type (messageId:${remoteMessage.messageId})"
            )
            return
        }

        val messageType = remoteMessage.data[FcmConstants.FIELD_MESSAGE_TYPE]
        Log.d(
            "CloudMessagingService",
            "New FCM (messageType=$messageType, messageId:${remoteMessage.messageId})"
        )

        when (messageType) {
            FcmConstants.MessageType.VISITS_CHANGED -> {

            }

            FcmConstants.MessageType.VISIT_REQUESTS_CHANGED -> {

            }
        }
    }

    object FcmConstants {
        const val FIELD_MESSAGE_TYPE = "MessageType"

        object MessageType {
            const val VISITS_CHANGED = "VisitsChanged"
            const val VISIT_REQUESTS_CHANGED = "VisitRequestsChanged"
        }
    }
}