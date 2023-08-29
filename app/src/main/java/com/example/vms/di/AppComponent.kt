package com.example.vms.di

import com.example.vms.login.Authentication
import com.example.vms.login.LoginActivity
import com.example.vms.login.LoginViewModel
import com.example.vms.user.UserManager
import dagger.Component
import retrofit2.Retrofit
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
    fun getUserManager(): UserManager
    fun getRetrofit(): Retrofit

    fun inject(activity: LoginActivity)
    fun inject(viewModel: LoginViewModel)
}