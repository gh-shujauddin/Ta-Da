package com.qadri.to_do.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.qadri.to_do.data.repository.RemoteTaskRepository
import com.qadri.to_do.data.repository.TaskRepository
import com.qadri.to_do.model.mappers.toTaskDto
import dagger.assisted.Assisted
import javax.inject.Inject

@HiltWorker
class BackgroundSyncWorker @Inject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val taskRepository: TaskRepository,
    private val onlineRepository: RemoteTaskRepository
) : CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {
        // 1. Get all unsynced tasks from room and create or update on cloud and mark sync to true locally
        val unSyncedTasks = taskRepository.getAllUnSyncedTasks()

        unSyncedTasks.forEach { taskEntity ->
            onlineRepository.updateTask(taskEntity.toTaskDto())
                .onSuccess {
                    taskRepository.markTaskAsSynced(taskEntity.id)
                }
                .onFailure {
                    Log.d(TAG, it.message.toString())
                }
        }

        // 2. Get all deleted tasks from room, delete that task from cloud, if delete success, then remove from room too
        val deletedTasks = taskRepository.getAllDeletedTasks()

        deletedTasks.forEach { taskEntity ->
            onlineRepository.deleteTask(taskEntity.toTaskDto())
                .onSuccess {
                    taskRepository.deleteTaskPermanently(taskEntity)
                }
                .onFailure {
                    Log.d(TAG, it.message.toString())
                }
        }

        // Get all tasks from cloud based on last synced time (set last synced time in prefs after every sync completed)

        return Result.success()
    }

    companion object {
        private const val TAG = "BGSyncWorker"
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