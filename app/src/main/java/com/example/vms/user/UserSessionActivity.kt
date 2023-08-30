package com.example.vms.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.vms.appComponent
import com.example.vms.login.LoginActivity

/**
 * Created by mśmiech on 28.08.2023.
 */
abstract class UserSessionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val signedIn = appComponent().getAuthentication().isSignedIn()
        if (signedIn) {
            onCreateWithUserSession(savedInstanceState)
        } else {
            launchLoginActivity()
            finish()
        }
    }

    abstract fun onCreateWithUserSession(savedInstanceState: Bundle?)

    override fun onStart() {
        super.onStart()
        val signedIn = appComponent().getAuthentication().isSignedIn()
        if (signedIn) {
            onStartWithUserSession()
        }
    }

    open fun onStartWithUserSession() {}

    private fun launchLoginActivity() = startActivity(Intent(this, LoginActivity::class.java))
}
