package com.example.vms.di

import com.example.vms.editvisit.EditVisitViewModel
import com.example.vms.home.HomeViewModel
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
    @Subcomponent.Builder
    interface Builder {
        fun userModule(userModule: UserModule): Builder

        fun build(): UserComponent
    }

    fun inject(factory: VisitDetailsViewModel.Factory)
    fun inject(viewModel: HomeViewModel)
    fun inject(factory: EditVisitViewModel.Factory)
    fun inject(factory: HomeViewModel.Factory)
}