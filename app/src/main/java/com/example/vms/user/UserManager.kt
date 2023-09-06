package com.example.vms.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.vms.di.UserComponent
import com.example.vms.di.UserModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Created by mśmiech on 06.08.2023.
 */
class UserManager(
    private val userComponentBuilder: UserComponent.Builder,
    context: Context
) {
    private var _userComponent: UserComponent? = null
    val userComponent: UserComponent?
        get() {
            if (_userComponent == null) {
                runBlocking {
                    tryRestoreUserSession()
                }
            }
            return _userComponent
        }

    private val dataStore = context.userManagerDataStore
    private val storeScope = CoroutineScope(Dispatchers.IO)
    private val mutex = Mutex()

    fun startUserSession(user: User) {
        _userComponent = createUserComponent(user)
        storeScope.launch {
            storeUser(user)
        }
    }

    private fun createUserComponent(user: User): UserComponent {
        return userComponentBuilder
            .userModule(UserModule(user))
            .build()
    }

    private suspend fun storeUser(user: User) {
        dataStore.edit {
            it[USER_EMAIL] = user.email
        }
    }

    private suspend fun tryRestoreUserSession() {
        mutex.withLock {
            if (_userComponent == null) {
                val user = tryRestoreUser()
                if (user != null) {
                    _userComponent = createUserComponent(user)
                }
            }
        }
    }

    private suspend fun tryRestoreUser(): User? {
        return dataStore.data.map {
            val userEmail = it[USER_EMAIL] as String
            User(userEmail)
        }.firstOrNull()
    }

    fun closeUserSession() {
        _userComponent = null
        storeScope.launch {
            clearStoredUser()
        }
    }

    private suspend fun clearStoredUser() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        val USER_EMAIL = stringPreferencesKey("user_email")
    }
}

val Context.userManagerDataStore: DataStore<Preferences> by preferencesDataStore(name = "userManager")