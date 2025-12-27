package com.qadri.to_do.data.usecase

import com.qadri.to_do.data.prefs.SyncSettingsManager
import com.qadri.to_do.data.repository.RemoteTaskRepository
import com.qadri.to_do.data.room.TaskDao
import com.qadri.to_do.model.mappers.toTaskDto
import com.qadri.to_do.model.mappers.toTaskEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

class BackgroundSyncUseCase(
    private val taskDao: TaskDao,
    private val onlineRepository: RemoteTaskRepository,
    private val syncSettingsManager: SyncSettingsManager
) {
    suspend operator fun invoke(): Result<Unit> {

        return try {
            val currentSyncTime = System.currentTimeMillis()
            var hasFailed = false

            // 1. Push local changes
            pushUnsyncedTasks().onFailure { hasFailed = true }
            pushDeletedTasks().onFailure { hasFailed = true }

            // 2. Pull remote changes
            pullRemoteChanges().onFailure { hasFailed = true }

            if (hasFailed) {
                Result.failure(IllegalStateException("Partial sync failure"))
            } else {
                syncSettingsManager.updateLastSyncTime(currentSyncTime)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 1.  Get all unsynced tasks from room and create or update on cloud and mark sync to true locally
    private suspend fun pushUnsyncedTasks(): Result<Unit> = coroutineScope {
        try {
            val unSyncedTasks = taskDao.getAllUnSyncedTasks()
            if (unSyncedTasks.isEmpty()) {
                return@coroutineScope Result.success(Unit)
            }

            val results = unSyncedTasks.map { taskEntity ->
                async {
                    onlineRepository.updateTask(taskEntity.toTaskDto())
                        .onSuccess {
                            taskDao.markTaskAsSynced(listOf(taskEntity.id), true)
                        }
                }
            }.awaitAll()

            val firstFailure = results.firstOrNull { it.isFailure }
            if (firstFailure != null) {
                return@coroutineScope Result.success(Unit)
            } else {
                return@coroutineScope Result.failure(firstFailure?.exceptionOrNull()!!)
            }
        } catch (ex: Exception) {
            return@coroutineScope Result.failure(ex)
        }
    }

    // 2. Get all deleted tasks from room, delete that task from cloud, if delete success, then remove from room too
    private suspend fun pushDeletedTasks(): Result<Unit> = coroutineScope {
        try {
            val deletedTasks = taskDao.getAllDeletedTasks()

            if (deletedTasks.isEmpty()) {
                return@coroutineScope Result.success(Unit)
            }

            val results = deletedTasks.map { taskEntity ->
                async {
                    onlineRepository.deleteTask(taskEntity.toTaskDto())
                        .onSuccess {
                            taskDao.deleteTaskPermanently(taskEntity)
                        }
                }
            }.awaitAll()

            val firstFailure = results.firstOrNull { it.isFailure }
            if (firstFailure != null) {
                return@coroutineScope Result.success(Unit)
            } else {
                return@coroutineScope Result.failure(firstFailure?.exceptionOrNull()!!)
            }
        } catch (ex: Exception) {
            return@coroutineScope Result.failure(ex)
        }
    }

    // Get all tasks from cloud based on last synced time (set last synced time in prefs after every sync completed)
    private suspend fun pullRemoteChanges(): Result<Unit> = coroutineScope {
        try {
            val lastSyncTime = syncSettingsManager.lastSyncTimeFlow.first()

            val result = onlineRepository.getAllTasks(lastSyncTime)

            if (result.isFailure) {
                return@coroutineScope Result.failure(result.exceptionOrNull()!!)
            }

            val remoteTasks = result.getOrThrow()

            remoteTasks.forEach { remoteTask ->
                val localTask = taskDao.getTask(remoteTask.id)

                when {
                    localTask == null -> {
                        // Add as new task and markAsSync
                        taskDao.insertTask(
                            task = remoteTask.toTaskEntity()
                                .copy(
                                    isSynced = true,
                                    isDeleted = false
                                )
                        )
                    }

                    // if localTime < remote time (update local)
                    localTask.lastUpdateTime < remoteTask.lastUpdateTime -> {
                        taskDao.updateItem(
                            remoteTask.toTaskEntity()
                                .copy(
                                    isSynced = true,
                                    isDeleted = false
                                )
                        )
                    }

                    // Local is newer → push later (do nothing here)
                    localTask.lastUpdateTime > remoteTask.lastUpdateTime -> {
                        // Intentionally skipped
                        // Will be handled by pushUnsyncedTasks()
                    }

                    else -> {
                        if (!localTask.isSynced) {
                            // else identical just mark as synced
                            taskDao.markTaskAsSynced(listOf(localTask.id), true)
                        }
                    }
                }
            }
            Result.success(Unit)
        } catch (ex: Exception) {
            return@coroutineScope Result.failure(ex)
        }
    }

    companion object {
        private const val TAG = "BGSyncUseCase"

    }
}