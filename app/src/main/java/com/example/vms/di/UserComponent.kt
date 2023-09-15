package com.example.vms.di

import com.example.vms.auditlog.AuditLogViewModel
import com.example.vms.editvisit.EditVisitViewModel
import com.example.vms.home.HomeViewModel
import com.example.vms.repository.VisitRepository
import com.example.vms.user.RegisterFCMTokenUseCase
import com.example.vms.user.UnregisterFCMTokenUseCase
import com.example.vms.visitdetails.VisitDetailsViewModel
import dagger.Subcomponent

/**
 * Created by mśmiech on 06.08.2023.
 */
@UserScope
@Subcomponent(modules = [
    UserModule::class
])
interface UserComponent {
    fun getVisitRepository(): VisitRepository
    fun getRegisterFCMTokenUseCase(): RegisterFCMTokenUseCase
    fun getUnregisterFCMTokenUseCase(): UnregisterFCMTokenUseCase

    @Subcomponent.Builder
    interface Builder {
        fun userModule(userModule: UserModule): Builder

        fun build(): UserComponent
    }

    fun inject(factory: VisitDetailsViewModel.Factory)
    fun inject(viewModel: HomeViewModel)
    fun inject(factory: EditVisitViewModel.Factory)
    fun inject(factory: HomeViewModel.Factory)
    fun inject(factory: AuditLogViewModel.Factory)
}