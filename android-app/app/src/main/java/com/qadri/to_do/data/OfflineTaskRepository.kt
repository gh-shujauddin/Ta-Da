package com.qadri.to_do.data

import android.util.Log
import androidx.room.Transaction
import com.qadri.to_do.data.repository.TaskRepository
import com.qadri.to_do.data.room.TaskDao
import com.qadri.to_do.data.room.TaskEntity
import com.qadri.to_do.model.Task
import com.qadri.to_do.model.dto.TaskDto
import com.qadri.to_do.model.mappers.toTask
import com.qadri.to_do.model.mappers.toTaskEntity
import com.qadri.to_do.workers.SyncScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineTaskRepository(
    private val taskDao: TaskDao,
    private val syncScheduler: SyncScheduler
) : TaskRepository {

    override fun getAllTask(): Flow<List<Task>> =
        taskDao.getAllTask().map {
            it.map { taskEntity ->
                taskEntity.toTask()
            }
        }

    override suspend fun getTask(id: Long): Task? = taskDao.getTask(id)?.toTask()

    override suspend fun insertTask(task: TaskDto, synced: Boolean) {
        taskDao.insertTask(task.toTaskEntity().copy(isSynced = synced))
        syncScheduler.syncNow()
    }

    override suspend fun insertLocalTask(task: Task) {
        taskDao.insertTask(task.toTaskEntity())
    }

    override suspend fun upsertItem(task: Task) {
        Log.d(TAG, "upsertItem: $task")
        if (taskDao.getTask(task.id) == null) {
            taskDao.insertTask(
                task.toTaskEntity()
                    .copy(
                        lastUpdateTime = System.currentTimeMillis(),
                        createdAt = System.currentTimeMillis()
                    )
            )
        } else {
            taskDao.updateItem(
                task.toTaskEntity()
                    .copy(lastUpdateTime = System.currentTimeMillis())
            )
        }
        syncScheduler.syncNow()
    }

    override suspend fun markItemAsCompleted(task: Task) {
        Log.d(TAG, "markItemAsComplted: $task")
        taskDao.toggleItemCompletion(
            id = task.id,
            lastUpdateTime = System.currentTimeMillis(),
            isCompleted = !task.isCompleted
        )
        syncScheduler.syncNow()
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.updateItem(
            task.toTaskEntity()
                .copy(
                    isDeleted = true,
                    lastUpdateTime = System.currentTimeMillis()
                )
        )
        syncScheduler.syncNow()
    }

    @Transaction
    override suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks(System.currentTimeMillis())
    }

    @Transaction
    override suspend fun deleteCompletedTasks() {
        taskDao.deleteCompletedTasks(System.currentTimeMillis())
    }

    override suspend fun markTaskAsSynced(id: Long, synced: Boolean) =
        taskDao.markTaskAsSynced(listOf(id), synced)

    override suspend fun getAllUnSyncedTasks(): List<TaskEntity> {
        return taskDao.getAllUnSyncedTasks()
    }

    override suspend fun getAllDeletedTasks(): List<TaskEntity> = taskDao.getAllDeletedTasks()

    override suspend fun updateAndMarkAsSynced(tasks: List<TaskDto>) {
        taskDao.updateAndMarkAsSynced(tasks.map { it.toTaskEntity() })
    }

    companion object {
        private const val TAG = "OfflineTaskRepository"
    }
}