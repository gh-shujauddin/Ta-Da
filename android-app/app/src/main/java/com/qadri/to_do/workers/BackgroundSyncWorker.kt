package com.qadri.to_do.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.qadri.to_do.data.usecase.BackgroundSyncUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class BackgroundSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val backgroundSyncUseCase: BackgroundSyncUseCase
) : CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {
        val result = backgroundSyncUseCase()
        return if (result.isFailure) {
            Log.d(TAG, "doWork: ${result.exceptionOrNull()}")
            Result.retry()
        } else {
            Log.d(TAG, "doWork: Success")
            Result.success()
        }
    }

    companion object {
        private const val TAG = "BackgroundSyncWorker"
    }
}

/**
 *  Implementation plan
 *  1. Get all unsynced tasks from room and create or update on cloud and mark sync to true locally
 *  2. Get all deleted tasks from room, delete that task from cloud, if delete success, then remove from room too
 *  3. Get all tasks from cloud based on last synced time (set last synced time in prefs after every sync completed)
 *  2. Update your room db if any task is missing
 *      2.1. Conflict resolution:
 *          - Room will keep the copy which is latest updated.
 *  3.
 *  4.!
 */