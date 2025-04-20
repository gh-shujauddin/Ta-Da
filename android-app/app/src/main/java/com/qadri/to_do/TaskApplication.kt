package com.qadri.to_do

import android.app.Application
import com.qadri.to_do.data.AppContainer
import com.qadri.to_do.data.AppDataContainer

class TaskApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}