package com.example.vms.di

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
}