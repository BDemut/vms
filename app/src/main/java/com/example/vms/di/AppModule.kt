package com.example.vms.di

import android.content.Context
import com.example.vms.login.Authentication
import com.example.vms.networking.AuthHeaderInterceptor
import com.example.vms.networking.RetrofitFactory
import com.example.vms.repository.api.Client
import com.example.vms.user.UserManager
import com.example.vms.user.UserProvider
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by mśmiech on 05.08.2023.
 */
@Module
class AppModule(val context: Context) {
    @Provides
    @Singleton
    fun provideContext(): Context = context

    @Provides
    @Singleton
    fun provideUserManager(
        userComponentBuilder: UserComponent.Builder,
        userProvider: UserProvider,
        context: Context
    ): UserManager = UserManager(userComponentBuilder, userProvider, context)

    @Provides
    @Singleton
    fun provideAuthentication(
        context: Context
    ) = Authentication(context)

    @Provides
    @Singleton
    fun provideAuthHeaderInterceptor(
        authentication: Authentication
    ) = AuthHeaderInterceptor(authentication)

    @Provides
    @Singleton
    fun provideRetrofit(authHeaderInterceptor: AuthHeaderInterceptor) =
        RetrofitFactory.createRetrofitInstance(authHeaderInterceptor)

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit) = retrofit.create(Client::class.java)

    @Provides
    @Singleton
    fun provideUserProvider(client: Client) = UserProvider(client)
}