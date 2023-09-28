package com.example.vms.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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
    private val userProvider: UserProvider,
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

    suspend fun startUserSession(cognitoUser: User) {
        val backendUser = userProvider.getUser()
        val user = User(
            email = backendUser?.email ?: cognitoUser.email,
            name = backendUser?.name ?: cognitoUser.name,
            isAdmin = backendUser?.isAdmin ?: cognitoUser.isAdmin
        )
        _userComponent = createUserComponent(user)
        storeScope.launch {
            storeUser(user)
        }
        _userComponent?.getRegisterFCMTokenUseCase()?.invoke()
    }

    private fun createUserComponent(user: User): UserComponent {
        return userComponentBuilder
            .userModule(UserModule(user))
            .build()
    }

    private suspend fun storeUser(user: User) {
        dataStore.edit {
            it[USER_EMAIL] = user.email
            user.name?.let { name -> it[USER_NAME] = name }
            if (user.isAdmin == null) {
                it.remove(IS_USER_ADMIN)
            } else {
                it[IS_USER_ADMIN] = user.isAdmin
            }
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
        CoroutineScope(Dispatchers.IO).launch {
            _userComponent?.getRegisterFCMTokenUseCase()?.invoke()
        }
    }

    private suspend fun tryRestoreUser(): User? {
        return dataStore.data.map {
            val userEmail = it[USER_EMAIL] as String
            val userName = if (it.contains(USER_NAME)) {
                it[USER_NAME] as String
            } else null
            val isAdmin = it[IS_USER_ADMIN]
            User(email = userEmail, name = userName, isAdmin = isAdmin)
        }.firstOrNull()
    }

    fun closeUserSession() {
        _userComponent = null
        storeScope.launch {
            clearStoredUser()
        }
        CoroutineScope(Dispatchers.IO).launch {
            _userComponent?.getUnregisterFCMTokenUseCase()?.invoke()
        }
    }

    private suspend fun clearStoredUser() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_NAME = stringPreferencesKey("user_name")
        val IS_USER_ADMIN = booleanPreferencesKey("is_user_admin")
    }
}

val Context.userManagerDataStore: DataStore<Preferences> by preferencesDataStore(name = "userManager")