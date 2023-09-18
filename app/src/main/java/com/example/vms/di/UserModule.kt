package com.example.vms.di

import com.example.vms.auditlog.GenerateAuditLogUseCase
import com.example.vms.repository.VisitRepository
import com.example.vms.repository.VisitRepositoryImpl
import com.example.vms.repository.api.Client
import com.example.vms.user.RegisterFCMTokenUseCase
import com.example.vms.user.UnregisterFCMTokenUseCase
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
    fun getVisitRepository(api: Client): VisitRepository = VisitRepositoryImpl(api)

    @Provides
    fun provideGenerateAuditLogUseCase() = GenerateAuditLogUseCase()

    @Provides
    fun provideRegisterFCMTokenUseCase(api: Client) = RegisterFCMTokenUseCase(api)

    @Provides
    fun provideUnregisterFCMTokenUseCase(api: Client) = UnregisterFCMTokenUseCase(api)
}