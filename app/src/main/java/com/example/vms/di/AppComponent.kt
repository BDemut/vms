package com.example.vms.di

import com.example.vms.login.Authentication
import com.example.vms.login.LoginActivity
import com.example.vms.login.LoginViewModel
import com.example.vms.user.UserManager
import com.example.vms.user.UserProvider
import dagger.Component
import javax.inject.Singleton

/**
 * Created by mśmiech on 05.08.2023.
 */
@Singleton
@Component(modules = [
    AppModule::class
])
interface AppComponent {
    fun userComponentBuilder(): UserComponent.Builder
    fun getAuthentication(): Authentication
    fun getUserProvider(): UserProvider
    fun getUserManager(): UserManager

    fun inject(activity: LoginActivity)
    fun inject(viewModel: LoginViewModel)
}