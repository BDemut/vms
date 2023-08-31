package com.example.vms.di

import com.example.vms.repository.TestVisitRepositoryImpl
import com.example.vms.repository.VisitRepository
import com.example.vms.user.User
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by mśmiech on 06.08.2023.
 */
@Module
class UserModule(private val _signInUser: User) {
    @Provides
    @UserScope
    @Named("signInUser")
    fun getSignInUser(): User = _signInUser

    @Provides
    @UserScope
    fun getVisitRepository(
        @Named("signInUser") signInUser: User
    ): VisitRepository = TestVisitRepositoryImpl(signInUser)
}