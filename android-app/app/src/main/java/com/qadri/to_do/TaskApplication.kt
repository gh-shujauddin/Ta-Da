package com.qadri.to_do

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkerFactory
import com.qadri.to_do.workers.SyncScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TaskApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: WorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    @Inject
    lateinit var syncScheduler: SyncScheduler

    override fun onCreate() {
        super.onCreate()
        syncScheduler.syncPeriodically()

    }

}