package com.example.vms.di

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
}