package com.example.vms.di

import android.content.Context
import com.example.vms.user.UserManager
import dagger.Module
import dagger.Provides
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
    fun provideUserManager(userComponentBuilder: UserComponent.Builder): UserManager
        = UserManager(userComponentBuilder)
}