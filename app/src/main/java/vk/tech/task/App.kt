package vk.tech.task

import android.app.Application
import vk.tech.task.di.components.AppComponent
import vk.tech.task.di.components.DaggerAppComponent


class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()
    }
}