package com.example.vms.login

import android.content.Context
import com.amazonaws.ClientConfiguration
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool


/**
 * Created by mśmiech on 18.08.2023.
 */
class LoginManager(
    private val context: Context
) {

    suspend fun initAWS() {
        val  clientConfiguration = ClientConfiguration()
        val poolId = ""
        val clientId = ""
        val userPool = CognitoUserPool(context, poolId, clientId, clientSecret, clientConfiguration)
//        val awsConfig: AWSConfiguration = AWSConfiguration()
        AWSMobileClient.getInstance().initialize(context)
    }

    suspend fun signIn(login: String, password: String) {
        AWSMobileClient.getInstance().signIn(
            login,
            password,

        )
    }
}