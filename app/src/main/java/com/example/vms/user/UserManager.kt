package com.example.vms.user

import com.example.vms.di.UserComponent
import com.example.vms.di.UserModule

/**
 * Created by mśmiech on 06.08.2023.
 */
class UserManager(
    private val userComponentBuilder: UserComponent.Builder
) {
    var userComponent: UserComponent? = null
        private set

    fun startUserSession(user: User) {
        userComponent = userComponentBuilder
            .userModule(UserModule(user))
            .build()
    }

    fun closeUserSession() {
        userComponent = null
    }
}