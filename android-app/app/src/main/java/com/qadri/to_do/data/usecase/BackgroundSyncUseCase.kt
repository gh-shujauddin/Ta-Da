package com.qadri.to_do.data.usecase

import android.util.Log
import com.qadri.to_do.data.prefs.SyncSettingsManager
import com.qadri.to_do.data.repository.RemoteTaskRepository
import com.qadri.to_do.data.room.TaskDao
import com.qadri.to_do.model.mappers.toTaskDto
import com.qadri.to_do.model.mappers.toTaskEntity
import kotlinx.coroutines.flow.first

class BackgroundSyncUseCase(
    private val taskDao: TaskDao,
    private val onlineRepository: RemoteTaskRepository,
    private val syncSettingsManager: SyncSettingsManager
) {
    suspend operator fun invoke(): Result<Unit> {

        return try {

            val currentSyncTime = System.currentTimeMillis()

            // 1. Get all unsynced tasks from room and create or update on cloud and mark sync to true locally
            val unSyncedTasks = taskDao.getAllUnSyncedTasks()

            unSyncedTasks.forEach { taskEntity ->
                onlineRepository.updateTask(taskEntity.toTaskDto())
                    .onSuccess {
                        taskDao.markTaskAsSynced(listOf(taskEntity.id), true)
                    }
                    .onFailure {
                        Log.d(TAG, it.message.toString())
                    }
            }

            // 2. Get all deleted tasks from room, delete that task from cloud, if delete success, then remove from room too
            val deletedTasks = taskDao.getAllDeletedTasks()

            deletedTasks.forEach { taskEntity ->
                onlineRepository.deleteTask(taskEntity.toTaskDto())
                    .onSuccess {
                        taskDao.deleteTaskPermanently(taskEntity)
                    }
                    .onFailure {
                        Log.d(TAG, it.message.toString())
                    }
            }

            // Get all tasks from cloud based on last synced time (set last synced time in prefs after every sync completed)
            val lastSyncTime = syncSettingsManager.lastSyncTimeFlow.first()

            val cloudTasks = onlineRepository.getAllTasks(lastSyncTime)
            return if (cloudTasks.isSuccess) {
                cloudTasks.getOrDefault(emptyList())
                    .forEach { remoteTask ->
                        val localTask = taskDao.getTask(remoteTask.id)
                        if (localTask == null) {
                            // Add as new task and markAsSync
                            taskDao.insertTask(
                                task = remoteTask.toTaskEntity().copy(isSynced = true)
                            )
                        } else {
                            // if localTime < remote time (update local)
                            if (localTask.lastUpdateTime < remoteTask.lastUpdateTime) {
                                taskDao.updateAndMarkAsSynced(listOf(remoteTask.toTaskEntity()))
                            } else if (localTask.lastUpdateTime > remoteTask.lastUpdateTime) {
                                // else if localtime > remote time (update remote)
                                onlineRepository.updateTask(localTask.toTaskDto())
                            } else {
                                // else identical just mark as synced
                                taskDao.markTaskAsSynced(listOf(localTask.id), true)
                            }
                        }

                    }
                syncSettingsManager.updateLastSyncTime(currentSyncTime)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Something went wrong"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "BGSyncUseCase"

    }
}