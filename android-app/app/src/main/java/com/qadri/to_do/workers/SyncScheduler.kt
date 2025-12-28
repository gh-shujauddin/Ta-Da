package com.qadri.to_do.workers

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SyncScheduler @Inject constructor(
    private val workManager: WorkManager
) {
    fun syncNow() {
        val request = OneTimeWorkRequestBuilder<BackgroundSyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        workManager.enqueueUniqueWork(
            "immediate_sync",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun syncPeriodically() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workerRequest = PeriodicWorkRequestBuilder<BackgroundSyncWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofMinutes(15))
            .build()

        workManager
            .enqueueUniquePeriodicWork(
                "background_sync",
                ExistingPeriodicWorkPolicy.KEEP,
                workerRequest
            )
    }

    private  fun cancelBackgroundSyncWorker() {
        workManager.cancelAllWorkByTag("background_sync")
        workManager.cancelAllWorkByTag("immediate_sync")

    }
}
