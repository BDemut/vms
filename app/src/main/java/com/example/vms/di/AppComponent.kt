package com.example.vms.di

import com.example.vms.home.HomeViewModel
import com.example.vms.login.Authentication
import com.example.vms.login.LoginActivity
import com.example.vms.login.LoginViewModel
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

    fun inject(activity: LoginActivity)
    fun inject(viewModel: LoginViewModel)
    fun inject(viewModel: HomeViewModel)
}