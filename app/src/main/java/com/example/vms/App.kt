package com.example.vms

import android.app.Application
import android.content.Context
import com.example.vms.di.AppComponent
import com.example.vms.di.AppModule
import com.example.vms.di.DaggerAppComponent
import com.example.vms.di.UserComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by mśmiech on 05.08.2023.
 */
class App: Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        setupAppComponent()
        CoroutineScope(Dispatchers.IO).launch {
            appComponent.getAuthentication().ensureInit()
        }
    }

    private fun setupAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}

fun Application.appComponent(): AppComponent =
    (this as App).appComponent

fun Context.appComponent(): AppComponent =
    (this.applicationContext as App).appComponent

fun Application.userComponent(): UserComponent =
    (this as App).appComponent.getUserManager().userComponent!!

fun Context.userComponent(): UserComponent =
    (this.applicationContext as App).appComponent.getUserManager().userComponent!!